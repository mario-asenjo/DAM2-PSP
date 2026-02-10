package org.example.server.auth;

public class AuthService {
    private final UserStore store;

    public AuthService(UserStore store) {
        this.store = store;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null) return false;
        return store.verifyPassword(username, password);
    }
}
