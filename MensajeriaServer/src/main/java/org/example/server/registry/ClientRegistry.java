package org.example.server.registry;

import org.example.server.core.ClientHandler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientRegistry {
    private final ConcurrentHashMap<String, ClientHandler> byUser = new ConcurrentHashMap<>();

    public void register(String username, ClientHandler handler) {
        byUser.put(username, handler);
    }

    public void unregister(String username) {
        if (username != null) byUser.remove(username);
    }

    public ClientHandler get(String username) {
        return byUser.get(username);
    }

    public Set<String> listUsers() {
        return Collections.unmodifiableSet(byUser.keySet());
    }
}
