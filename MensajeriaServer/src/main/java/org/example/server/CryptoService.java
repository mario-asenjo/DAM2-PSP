package org.example.server;

/**
 * Strategy pattern for encrypt data and decrypt data
 * Allows changing AES-CGM for another without touching the server.
 */
public interface CryptoService {
    byte[] encrypt(byte[] plaintext) throws Exception;
    byte[] decrypt(byte[] ciphertext) throws Exception;
}
