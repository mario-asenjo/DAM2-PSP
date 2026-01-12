package org.example.server;

import java.util.Base64;

/**
 * Reads crypto config from environment variables
 */
public final class CryptoConfig {
    private CryptoConfig() {}

    /**
     * Reads CHAT_PSK_B64 and returns key in bytes.
     * Recommended: 32 Bytes (AES-256)
     */
    public static byte[] loadPskFromEnv() {
        String base64 = System.getenv("CHAT_PSK_B64");
        byte[] key = null;

        if (base64 == null || base64.isBlank()) {
            throw new IllegalStateException("Missing env CHAT_PSK_B64 (base 64 AES key)");
        }

        key = Base64.getDecoder().decode(base64);
        if (!(key.length == 16 || key.length == 24 || key.length == 32)) {
            throw new IllegalStateException("Invalid AES key length: " + key.length + " bytes");
        }
        return key;
    }
}
