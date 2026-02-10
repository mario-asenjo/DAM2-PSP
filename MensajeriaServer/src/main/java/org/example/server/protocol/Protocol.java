package org.example.server.protocol;

import com.google.gson.JsonObject;
import org.example.server.auth.AuthService;
import org.example.server.registry.ClientRegistry;

public class Protocol {
    public static final class ClientContext {
        public final long clientId;
        public String username;
        public final AuthService authService;
        public final ClientRegistry registry;

        public ClientContext(long clientId, AuthService authService, ClientRegistry registry) {
            this.clientId = clientId;
            this.authService = authService;
            this.registry = registry;
        }
    }

    /*public JsonObject handle(JsonObject msg, ClientContext ctx) {
        String type = msg.has("type") ? msg.get("type").getAsString() : "";

        return switch (type) {
            case "AUTH" -> handleAuth(msg, ctx);
            case "MSG" -> handleMsgLegacy(msg, ctx);
            default -> ack(type, false, "Unknown type");
        };
    }*/
}
