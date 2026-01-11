package org.example.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Accepts TCP connections and creates a thread for client.
 */
public class ChatServer {
    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    /** Genera ID únicos de cliente de forma thread-safe */
    private static final AtomicLong clientSeq = new AtomicLong(0);

    /** Genera ID únicos de mensaje de forma thread-safe */
    static final AtomicLong msqSeq = new AtomicLong(0);

    public static void main(String[] args) throws IOException {
        int port = 5000;
        Socket socket = null;
        long clientId = -1;
        ClientHandler handler = null;
        Thread thread = null;
        ServerSocket serverSocket = null;

        if (args.length >= 1) port = Integer.parseInt(args[0]);

        try {
            serverSocket = new ServerSocket(port);
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
