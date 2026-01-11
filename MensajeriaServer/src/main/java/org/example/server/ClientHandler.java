package org.example.server;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Atiende a un cliente.
 * LEe frames (len + payload), parsea JSON y responde ACK.
 */
public class ClientHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private final long clientId;
    private final Socket socket;
    private String username = null;

    public ClientHandler(long clientId, Socket socket) {
        this.clientId = clientId;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        byte[] payload = null;
        JsonObject msg = null;
        String type = null;
        String exception_message = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            while (true) {
                payload = FrameCodec.readFrame(in);
                msg = ProtocolCodec.decode(payload);

                type = msg.has("type") ? msg.get("type").getAsString() : "";
                switch (type) {
                    case "AUTH" -> handleAuth(msg, out);
                    case "MSG" -> handleMsg(msg, out);
                    default -> sendAck(out, type, false, "Unknown type");
                }
            }
        } catch (IOException e) {
            exception_message = e.getMessage() == null ? "" : e.getMessage();

            if (exception_message.contains("Stream closed while reading")) {
                log.info("Client #{} disconnected normally", clientId);
            } else if (exception_message.contains("Connection reset by peer")) {
                log.info("Client #{} disconnected abruptly (reset by peer)", clientId);
            } else {
                log.warn("Client #{} IO error: {}", clientId, exception_message);
            }
        } catch (Exception e) {
            log.info("Client #{} handler stopeped: {}", clientId, e.getMessage());
            log.debug("Details for client #{}:", clientId, e);
        } finally {
            try { if (in != null) in.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * ACK de aplicación, confirma que se recibió y se procesó un mensaje completo.
     * @param out
     * @param forType
     * @param ok
     * @param message
     * @throws Exception
     */
    private void sendAck(OutputStream out, String forType, boolean ok, String message) throws Exception {
        JsonObject ack = new JsonObject();
        ack.addProperty("type", "ACK");
        ack.addProperty("for", forType == null ? "" : forType);
        ack.addProperty("ok", ok);
        if (message != null && !message.isBlank()) ack.addProperty("msg", message);
        FrameCodec.writeFrame(out, ProtocolCodec.encode(ack));
    }

    /**
     * AUTH: valida el username y lo guarda en this.username.
     * @param message: JSON Message received.
     * @param out: Socket outputStream to write on.
     * @throws Exception: When helpers fail.
     */
    private void handleAuth(JsonObject message, OutputStream out) throws Exception {
        JsonObject ack = null;

        if (!message.has("username")) {
            sendAck(out, "AUTH", false, "Missing username");
            return ;
        }

        this.username = message.get("username").getAsString();
        log.info("Client #{} authenticated as '{}'", clientId, username);

        ack = new JsonObject();
        ack.addProperty("type", "ACK");
        ack.addProperty("for", "AUTH");
        ack.addProperty("ok", true);
        ack.addProperty("msg", "Welcome! " + username);
        FrameCodec.writeFrame(out, ProtocolCodec.encode(ack));
    }

    /**
     * De momento no se enrutan los mensajes, solo validamos y hacemos ACK.
     * @param message
     * @param out
     * @throws Exception
     */
    private void handleMsg(JsonObject message, OutputStream out) throws Exception {
        String from = null;
        String to = null;
        long ts = -1;
        String text = null;
        long id = -1;
        JsonObject ack = null;

        if (username == null) {
            sendAck(out, "MSG", false, "Not authenticated");
            return ;
        }

        from = message.has("from") ? message.get("from").getAsString() : "";
        to = message.has("to") ? message.get("to").getAsString() : "";
        ts = message.has("ts") ? message.get("ts").getAsLong() : -1;
        text = message.has("text") ? message.get("text").getAsString() : "";

        id = ChatServer.msqSeq.incrementAndGet();
        log.info("MSG #{} from='{}' to='{}' ts={} text_len={}", id, from, to, ts, text.length());

        ack = new JsonObject();
        ack.addProperty("type", "ACK");
        ack.addProperty("for", "MSG");
        ack.addProperty("ok", true);
        ack.addProperty("id", "srv-" + id);
        FrameCodec.writeFrame(out, ProtocolCodec.encode(ack));
    }
}
