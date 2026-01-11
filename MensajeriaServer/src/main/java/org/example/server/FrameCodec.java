package org.example.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Frame: 4 bytes big-endian (int) con longitud + payload bytes.
 * Convierte TCP (stream) en "mensajes" definidos por nosotros.
 */
public class FrameCodec {
    private FrameCodec() {}

    public static final int HEADER_LEN = 4;
    public static final int MAX_FRAME = 1_048_576; // Exactamente 1MiB para evitar abusos.

    /**
     * Lee exactamente n bytes del stream (bloqueante).
     * Si el stream se cierra antes, lanza IOException.
     * @param in
     * @param n
     * @return
     * @throws IOException
     */
    public static byte[] readExact(InputStream in, int n) throws IOException {
        byte[] buffer = new byte[n];
        int offset = 0;
        int bytes_read = -1;

        while (offset < n) {
            bytes_read = in.read(buffer, offset, n - offset);
            if (bytes_read == -1) {
                throw new IOException("Stream closed while reading " + n + " byter (got " + offset + ")");
            }
            offset += bytes_read;
        }
        return buffer;
    }

    /**
     * Escribe header (4 bytes big-endian) + payload.
     * @param out
     * @param payload
     * @throws IOException
     */
    public static void writeFrame(OutputStream out, byte[] payload) throws IOException {
        byte[] header = null;
        if (payload == null) payload = new byte[0];
        if (payload.length <= 0 || payload.length > MAX_FRAME) {
            throw new IOException("Invalid payload length: " + payload.length);
        }

        header = ByteBuffer.allocate(HEADER_LEN).putInt(payload.length).array();
        out.write(header);
        out.write(payload);
        out.flush(); // solo al principio para evitar dudas.
    }

    /**
     * Lee 4 bytes, interpreta la longitud y luego lee el payload completo.
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readFrame(InputStream inputStream) throws IOException {
        byte[] header = readExact(inputStream, HEADER_LEN);
        int length = ByteBuffer.wrap(header).getInt();

        if (length <= 0 || length > MAX_FRAME) {
            throw new IOException("Invalid frame length: " + length);
        }
        return readExact(inputStream, length);
    }
}
