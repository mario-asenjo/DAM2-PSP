"""

"""


from __future__ import annotations

import threading
import time
from collections import deque
from dataclasses import dataclass
from typing import Optional, Dict, Any

from PySide6.QtCore import QObject, QThread, Slot, QTimer

from app.core.state import SessionState
from app.gui.main_window import MainWindow, ConnectionParams
from app.network.net_worker import NetworkWorker, ConnectRequest
from app.network.transport import TlsConfig


def _now_ms() -> int:
    """Return current Unix epoch time in milliseconds."""
    return int(time.time() * 1000)


class ChatController(QObject):
    """
    Main UI Controller.

    Responsibilities:
    - Wire UI events to actions (connect, send, mode switch).
    - Own the QThread + NetworkWorker lifecycle.
    - Render server events into the UI via MainWindow public API.

    Notes:
        - No blocking calls happen on the UI thread.
        - NetworkWorker runs entirely in a QThread and communicates via Qt Signals.
    """
    def __init__(self, window: MainWindow) -> None:
        """Initialize controller and attach it to a MainWindow."""
        super().__init__(window)
        self._window = window

        self._log_buf = deque()
        self._log_timer = QTimer(self._window)
        self._log_timer.setInterval(150)
        self._log_timer.timeout.connect(self._flush_logs)
        self._log_timer.start()

        self._thread: Optional[QThread] = None
        self._worker: Optional[NetworkWorker] = None
        self._state: SessionState = SessionState()

        # Enganchamos las callbacks de UI -> controller handlers
        self._window.on_connect_clicked = self.on_connect_clicked    # type: ignore
        self._window.on_send_clicked = self.on_send_clicked          # type: ignore
        self._window.on_public_clicked = self.on_public_clicked      # type: ignore

        self._window.append_chat_line("[ui] List. Pulsa Conectar.")

    # UI Handlers
    @Slot()
    def on_connect_clicked(self) -> None:
        """Handle Connect/Disconnect button."""
        if not self._state.connected:
            self._connect()
        else:
            self._disconnect("Desconectado por el usuario")

    @Slot()
    def on_send_clicked(self) -> None:
        """Handle Send button / Enter in message box."""
        if not self._state.connected or self._worker is None:
            self._window.append_chat_line("[ui] No conectado.")
            return

        text: str = self._window.get_message_text().strip()
        if not text:
            return

        params: ConnectionParams = self._window.get_connection_params()
        to_user: str = self._selected_target_user(default="bob")

        msg: Dict[str, Any] = {
            "type": "MSG",
            "from": params.username,
            "to": to_user,
            "ts": _now_ms(),
            "text": text
        }

        self._worker.send_message(msg)
        self._window.append_chat_line(f"[me -> {to_user}] {text}")
        self._window.clear_message_box()

    @Slot()
    def on_public_clicked(self) -> None:
        """Switch UI back to public mode (UI-only for now)."""
        self._window.append_chat_line("[ui] Modo públic (UI).")
        # Si quieres: aquí podrías limpiar selección de user list.
        # self._window.clear_user_selection() # (si se implementa en algun momento: Preguntar a Luis)

    @Slot()
    def _flush_logs(self) -> None:
        n = 0
        while self._log_buf and n < 20:  # máximo 20 líneas por tick
            self._window.append_chat_line(self._log_buf.popleft())
            n += 1

    @Slot(str)
    def _on_worker_log(self, s: str) -> None:
        self._log_buf.append(f"[net] {s}")

    # Connection management
    def _connect(self) -> None:
        """Start thread + worker and connect to server."""
        self._window.append_chat_line(f"[dgb] UI thread ident={threading.get_ident()}")

        params: ConnectionParams = self._window.get_connection_params()
        if not params.host:
            self._window.append_chat_line("[ui] Host obligatorio.")
            return
        if not params.username:
            self._window.append_chat_line("[ui] Usuario obligatorio.")
            return

        if self._thread is not None or self._worker is not None:
            self._window.append_chat_line("[ui] Ya hay un hilo en marcha, cancelando...")
            self._disconnect("Reset interno")
            return

        self._state.username = params.username
        self._state.connected = False
        self._state.authenticated = False

        worker = NetworkWorker()

        # Wire signals from worker -> controller/UI
        worker.connected.connect(self._on_socket_connected)
        worker.disconnected.connect(self._on_disconnected)
        worker.received.connect(self._on_received)
        worker.log.connect(self._on_worker_log)

        # Build the request (TLS ready, disabled by default)
        req: ConnectRequest = ConnectRequest(
            host=params.host,
            port=params.port,
            username=params.username,
            password=params.password,
            tls=TlsConfig(
                enabled=True,
                verify=True,
                ca_file="cert/server.crt",
                server_hostname="localhost"
            )
        )

        worker.start_connect(req)
        self._worker = worker
        self._window.append_chat_line(f"[ui] Conectado a {params.host}:{params.port}...")

    def _disconnect(self, reason: str) -> None:
        """Stop worker and thread and update UI."""
        self._window.append_chat_line(f"[ui] {reason}")

        if self._worker is not None:
            try:
                self._worker.close()
                self._worker.wait(500)  # esperar a que termine el hilo
            except Exception:
                pass

        if self._thread is not None:
            self._thread.quit()
            self._thread.wait(500)

        self._worker = None
        self._thread = None
        self._state.connected = False
        self._state.authenticated = False
        self._window.set_connected(False, "Desconectado")

    def _selected_target_user(self, default: str) -> str:
        """Return selected user from UI list or a default."""
        # Todavía no la exponemos.
        if hasattr(self._window, "selected_user"):
            user = getattr(self._window, "selected_user")()
            if isinstance(user, str) and user.strip():
                return user.strip()

        return default

    # Worker Callbacks
    @Slot()
    def _on_socket_connected(self) -> None:
        """Called when underlying socket is connected."""
        self._state.connected = True
        self._window.set_connected(True, "Conectado (socket)")
        self._window.append_chat_line("[ui] Socket conectado, AUTH enviado...")

    @Slot(str)
    def _on_disconnected(self, reason: str) -> None:
        """Called when worker reports disconnection."""
        self._disconnect(f"Desconectado: {reason}")

    @Slot(dict)
    def _on_received(self, msg: Dict[str, Any]) -> None:
        """Render any incoming server message."""
        m_type: str = str(msg.get("type", ""))

        if m_type == "AUTH_OK":  # No es AUTH tal cual?
            self._state.authenticated = True
            user = msg.get("username", "")
            text = msg.get("msg", "")
            self._window.append_chat_line(f"[server] AUTH_OK {user} {text}".strip())
            return

        if m_type == "ACK":
            ok = msg.get("ok", False)
            for_type: Any = msg.get("for", "")
            extra: Any = msg.get("msg", "")
            self._window.append_chat_line(f"[server] ACK for={for_type} ok={ok} {extra}".strip())
            return

        if m_type == "ERR":
            self._window.append_chat_line(f"[server] ERR {msg.get('msg', '')}".strip())
            return

        self._window.append_chat_line(f"[server] {msg}")

    def shutdown(self) -> None:
        """Close network resources before exiting the app."""
        self._disconnect("Cerrando aplicación")