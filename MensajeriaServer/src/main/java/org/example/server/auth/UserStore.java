package org.example.server.auth;

import java.util.Map;

public class UserStore {
    private final Map<String, String> users = Map.of(
            "mario", "contraseña_mario",
            "bob", "1234",
            "carlos", "1234"
    );

    public boolean verifyPassword(String username, String password) {
        String expected = users.get(username);
        return expected != null && expected.equals(password);
    }
}
