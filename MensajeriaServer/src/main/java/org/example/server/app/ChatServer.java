package org.example.server.app;

import org.example.server.core.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Accepts TCP connections and creates a thread for client.
 */
public class ChatServer {
    /** KeyStore resource path*/
    private static final String KEYSTORE_RESOURCE_PATH = "certs/server.p12";
    private static final char[] KEYSTORE_PASSWORD = "morcilla01".toCharArray();

    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    /** Genera ID únicos de cliente de forma thread-safe */
    public static final AtomicLong clientSeq = new AtomicLong(0);

    /** Genera ID únicos de mensaje de forma thread-safe */
    public static final AtomicLong msqSeq = new AtomicLong(0);

    private static SSLServerSocket createSSLServerSocket(int port) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (InputStream is = ChatServer.class.getClassLoader().getResourceAsStream(KEYSTORE_RESOURCE_PATH)) {
            if (is == null) {
                throw new IllegalStateException("KeyStore not found in resources: " + KEYSTORE_RESOURCE_PATH);
            }
            keyStore.load(is, KEYSTORE_PASSWORD);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEYSTORE_PASSWORD);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory factory = ctx.getServerSocketFactory();

        // De forma opcional podemos restringir protocolos.
        // serverSocket.setEnabledProtocols(new String[] {"TLSv1.3", "TLSv1.2"});

        return (SSLServerSocket) factory.createServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        Socket socket = null;
        long clientId = -1;
        ClientHandler handler = null;
        Thread thread = null;
        SSLServerSocket serverSocket = null;

        if (args.length >= 1) port = Integer.parseInt(args[0]);

        try {
            serverSocket = createSSLServerSocket(port);
            log.info("ChatServer listening on port {}", port);

            while (true) {
                socket = serverSocket.accept();
                clientId = clientSeq.incrementAndGet();
                log.info("Accepted client #{} from {}", clientId, socket.getRemoteSocketAddress());

                handler = new ClientHandler(clientId, socket);
                thread = new Thread(handler, "client-" + clientId);
                thread.start();
            }
        } catch (IOException e) {
            log.error("Fatal server socket error", e);
        } catch (SecurityException | IllegalArgumentException e) {
            log.error("Invalid server configuration", e);
        } catch (Exception e) {
            log.error("Fatal server error", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    log.warn("Error closing ServerSocket", e);
                }
            }
        }
    }
}
