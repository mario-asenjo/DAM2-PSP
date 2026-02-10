from __future__ import annotations

from socket import socket
import struct


HEADER_LEN: int = 4
MAX_FRAME: int = 1_048_576 # Exactamente 1MiB para no permitir abusos.


def read_exact(sock: socket, n: int) -> bytes:
    """
    It reads exactly n bytes from Socket.

    Important:
    - sock.recv(k) can return less than k bytes (TCP is stream).
    - That's why we accumulate until having n bytes or until connection is closed.

    :raises ConnectionError: if socket closes before completing n bytes.
    """
    chunks: list[bytes] = []
    remainder: int = n

    while remainder > 0:
        data = sock.recv(remainder)
        if data == b"": # connection closed
            raise ConnectionError(f"Socket closed while reading {n} bytes")
        chunks.append(data)
        remainder -= len(data)
    return b"".join(chunks)


def read_frame(sock: socket) -> bytes:
    """
    Read a frame: [4 bytes length big-endian] + [payload].

    struct.unpack('>I', header) => returns length where length is 0..2*32-1.
    """
    header = read_exact(sock, HEADER_LEN)
    (length, ) = struct.unpack(">I", header)

    if 0 <= length > MAX_FRAME:
        raise ValueError(f"Invalid frame length: {length}")

    return read_exact(sock, length)


def send_frame(sock: socket, payload: bytes) -> None:
    """
    Sends a frame: header (len) + payload.

    sock.sendall(...) sends all or raises exception.
    """
    if payload is None:
        payload = b""
    if 0 <= len(payload) > MAX_FRAME:
        raise ValueError(f"Invalid payload length: {len(payload)}")

    header = struct.pack(">I", len(payload))
    sock.sendall(header + payload)