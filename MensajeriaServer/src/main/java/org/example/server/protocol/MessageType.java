package org.example.server.protocol;

public enum MessageType {
    AUTH, AUTH_OK,
    MSG_PUBLIC, EVT_PUBLIC,
    MSG_PRIVATE, EVT_PRIVATE,
    TYPING, EVT_TYPING,
    ERR,
    MSG,
    ACK
}
