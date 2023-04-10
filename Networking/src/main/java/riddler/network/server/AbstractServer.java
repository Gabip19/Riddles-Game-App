package riddler.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private final int port;
    private ServerSocket server = null;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for clients ...");
                Socket client = server.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void processRequest(Socket client);
}
