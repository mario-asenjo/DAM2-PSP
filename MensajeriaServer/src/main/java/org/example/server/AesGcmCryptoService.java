package org.example.server;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES-GCM:
 * - Nonce 12 bytes aleatorio por mensaje
 * - Tag 128 bits
 *
 * Formato output:
 * [nonce_len=12 (1 byte)] + [nonce 12 bytes] + [ciphertext||tag]
 */
public final class AesGcmCryptoService implements CryptoService {
    private static final int NONCE_LEN = 12;
    private static final int TAG_BITS = 128;

    private final SecretKeySpec keySpec;
    private final SecureRandom rng = new SecureRandom();

    public AesGcmCryptoService(byte[] key) {
        this.keySpec = new SecretKeySpec(key, "AES");
    }

    @Override
    public byte[] encrypt(byte[] plaintext) throws Exception {
        byte[] nonce = new byte[NONCE_LEN];
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] ct = null;
        byte[] out = null;

        rng.nextBytes(nonce);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BITS, nonce));
        ct = cipher.doFinal(plaintext);
        out = new byte[1 + NONCE_LEN + ct.length];
        out[0] = (byte) NONCE_LEN;
        System.arraycopy(nonce, 0, out, 1, NONCE_LEN);
        System.arraycopy(ct, 0, out, 1 + NONCE_LEN, ct.length);
        return out;
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) throws Exception {
        int nonceLen = -1;
        byte[] nonce = null;
        int ctOffset = -1;
        int ctLen = -1;
        byte[] ct = null;
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        if (ciphertext == null || ciphertext.length < 1 + NONCE_LEN + 16) {
            throw new IllegalArgumentException("Ciphertext too short"); // Mínimo 16 bytes tag.
        }

        nonceLen = ciphertext[0] & 0xff; // Pasamos de byte a unsigned
        if (nonceLen != NONCE_LEN) {
            throw new IllegalArgumentException("Unexpected token length: " + nonceLen);
        }

        nonce = new byte[NONCE_LEN];
        System.arraycopy(ciphertext, 1, nonce, 0, NONCE_LEN);

        ctOffset = 1 + NONCE_LEN;
        ctLen = ciphertext.length - ctOffset;
        ct = new byte[ctLen];
        System.arraycopy(ciphertext, ctOffset, ct, 0, ctLen);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_BITS, nonce));

        return cipher.doFinal(ct); // si tag no ha cuadrado -> Exception
    }
}
