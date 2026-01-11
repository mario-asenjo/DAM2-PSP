import logging
from socket import socket, create_connection, SHUT_RDWR
from threading import Event, Thread
import time
from typing import Any, Dict, Callable
from network.frame_codec import read_frame, send_frame
from network.protocol_codec import encode, decode

# Logging configuration
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(threadName)s] %(levelname)s %(message)s",
    datefmt="%H:%M:%S"
)
log = logging.getLogger("client")


def reciever_loop(sock: socket, stop_evt: Event) -> None:
    """
    Reads frames in a loop and prints them
    """
    payload: bytes
    message: Dict[str, Any]
    try:
        while not stop_evt.is_set():
            payload = read_frame(sock)
            message = decode(payload)
            log.info("RECV %s", message)
    except (ConnectionError, OSError) as e:
        if not stop_evt.is_set():
            log.info("Receiver stopped unexpectedly: %s", e)
        else:
            log.info("Receiver stopped (normal shutdown)")
        stop_evt.set()
    except Exception as e:
        log.info("Receiver stopped (unexpected): %s", e)
        stop_evt.set()


def main() -> None:
    host: str = "127.0.0.1"
    port: int = 5000
    username: str = "mario"

    sock: socket = create_connection((host, port))
    log.info("Connected to %s:%d", host, port)

    stop_evt: Event = Event()
    thread = Thread(target=reciever_loop, args=(sock, stop_evt), name="receiver", daemon=True)
    thread.start()

    # Auth
    auth: Dict[str, Any] = {"type": "AUTH", "username": username}
    send_frame(sock, encode(auth))
    log.info("SENT AUTH")

    # Enviar varios MSG Seguidos (Sin Sleep) para probar framing.
    now_ms: Callable[[], int] = lambda: int(time.time() * 1000)

    for i in range(10):
        msg: Dict[str, Any] = {
            "type": "MSG",
            "from": username,
            "to": "bob",
            "ts": now_ms(),
            "text": f"Hola bob!#{i}"
        }
        send_frame(sock, encode(msg))
        log.info(f"SENT MESSAGE #{i}!")

    # Mensaje grande para forzar fragmentación
    big_text: str = "X" * 50_000
    big_msg: Dict[str, Any] = {
        "type": "MSG",
        "from": username,
        "to": "bob",
        "ts": now_ms(),
        "text": big_text
    }
    send_frame(sock, encode(big_msg))
    log.info("SENT BIG MESSAGE! text_len=%d", len(big_text))

    # Esperamos un poco para respuestas ACK lleguen
    time.sleep(4)

    stop_evt.set()
    try:
        sock.shutdown(SHUT_RDWR)
    except Exception:
        pass
    sock.close()
    thread.join(timeout=0.1) # Esperamos a que el receiver salga
    log.info("Closed!")


if __name__ == "__main__":
    main()
