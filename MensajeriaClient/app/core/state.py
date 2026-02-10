from __future__ import annotations
from dataclasses import dataclass


@dataclass
class SessionState:
    """In-memory state for the current session."""
    connected: bool = False
    authenticated: bool = False
    username: str = ""
