"""

"""
from __future__ import annotations

import ssl
from dataclasses import dataclass
import socket
from pathlib import Path
from typing import Optional


@dataclass(frozen=True)
class TlsConfig:
    enabled: bool = False
    ca_file: Optional[str] = None
    server_hostname: Optional[str] = None
    verify: bool = True

def open_connection(host: str, port: int, tls: TlsConfig) -> socket.socket:
    raw: socket.socket = socket.create_connection((host, port), timeout=5)

    if not tls.enabled:
        return raw

    # Context TLS
    if tls.verify:
        cafile = tls.ca_file
        if cafile is None:
            raise RuntimeError("TLS verify enabled but ca_file is None")
        cafile_path = Path(cafile)
        if not cafile_path.is_absolute():
            # relativo a la raíz del paquete /app
            base_dir = Path(__file__).resolve().parents[1] # .../app
            cafile_path = base_dir.parent / cafile_path # .../ (raíz proyecto) + relativo

        ctx = ssl.create_default_context(cafile=str(cafile_path))
        if tls.server_hostname is None:
            server_hostname = host
        else:
            server_hostname = tls.server_hostname
    else:
        ctx = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
        ctx.check_hostname = False
        ctx.verify_mode = ssl.CERT_NONE
        server_hostname = None

    s_sock = ctx.wrap_socket(raw, server_hostname=server_hostname)
    return s_sock
