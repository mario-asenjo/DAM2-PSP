from json import loads, dumps
from typing import Any, Dict


def encode(obj: Dict[str, Any]) -> bytes:
    """
    Converts dict -> JSON UTF-8.
    ensure_ascii=False allows emojis without escaping.
    separators compacts JSON (fewer bytes).
    """
    string: str = dumps(obj, ensure_ascii=False, separators=(",", ":"))
    return string.encode("utf-8")


def decode(data: bytes) -> Dict[str, Any]:
    """
    Converts bytes UTF-8 with JSON -> dict.
    """
    string: str = data.decode("utf-8")
    return loads(string)
