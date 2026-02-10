"""

"""


from __future__ import annotations

import socket
from dataclasses import dataclass
import queue
from threading import Event
from typing import Dict, Any, Optional

from PySide6.QtCore import QObject, Signal, Slot, QThread

from app.network.frame_codec import send_frame, read_frame
from app.network.protocol_codec import decode, encode
from app.network.transport import TlsConfig, open_connection


@dataclass(frozen=True)
class ConnectRequest:
    """Connection paameters passed from UI thread to the Network thread"""
    host: str
    port: int
    username: str
    password:  str = ""
    tls: TlsConfig = TlsConfig()

class NetworkWorker(QThread):
    """
    Network thread that owns the socket (read + write).

    Design rule:
        Only this thread may call recv/send on the socket.
        UI thread communicates via signals + an outgoing queue.
    """
    connected = Signal()
    disconnected = Signal(str)
    received = Signal(dict)
    log = Signal(str)

    def __init__(self) -> None:
        super().__init__()
        self._req: Optional[ConnectRequest] = None
        self._sock: Optional[socket.socket] = None
        self._stop = Event()
        self._outgoing: "queue.Queue[Dict[str, Any]]" = queue.Queue()

    def start_connect(self, req: ConnectRequest) -> None:
        """Start the thread and connect using the given request."""
        if self.isRunning():
            raise RuntimeError("NetworkWorker already running")
        self._req = req
        self._stop.clear()
        self.start()

    @Slot(dict)
    def send_message(self, msg: Dict[str, Any]) -> None:
        """Enqueue a message to be sent by the network thread."""
        self._outgoing.put(msg)

    @Slot()
    def close(self) -> None:
        """Request thread shutdown and close socket."""
        self._stop.set()
        self._shutdown_socket()

    def run(self) -> None:
        """Thread entrypoint: connect, AUTH, then loop read/write."""
        try:
            if self._req is None:
                self.disconnected.emit("Missing ConnectRequest")
                return

            req = self._req
            self._sock = open_connection(req.host, req.port, req.tls)

            # IMPORTANT: small timeout so we can periodically check outgoing queue & stop flag
            self._sock.settimeout(0.2)

            self.log.emit(f"Connected to {req.host}:{req.port} (tls={req.tls.enabled}, verify={req.tls.verify})")
            self.connected.emit()

            # AUTH
            auth: Dict[str, Any] = {"type": "AUTH", "username": req.username}
            if req.password:
                auth["password"] = req.password
            send_frame(self._sock, encode(auth))
            self.log.emit("SENT AUTH")

            # Main loop
            while not self._stop.is_set():
                # 1) Send pending outgoing messages
                self._drain_outgoing()

                # 2) Read incoming frame (with timeout)
                try:
                    payload = read_frame(self._sock)
                except TimeoutError:
                    continue
                except socket.timeout:
                    continue

                msg = decode(payload)
                self.received.emit(msg)

        except Exception as e:
            self.disconnected.emit(str(e))
        finally:
            self._shutdown_socket()

    def _drain_outgoing(self) -> None:
        """Send all queued messages."""
        if self._sock is None:
            return

        while True:
            try:
                msg = self._outgoing.get_nowait()
            except queue.Empty:
                return

            try:
                send_frame(self._sock, encode(msg))
            except Exception as e:
                self.log.emit(f"send failed: {e}")

    def _shutdown_socket(self) -> None:
        """Close the socket safely."""
        s = self._sock
        self._sock = None
        if s is None:
            return
        try:
            try:
                s.shutdown(2)
            except Exception:
                pass
            s.close()
        except Exception:
            pass