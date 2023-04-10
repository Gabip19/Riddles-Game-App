package riddler.network.server;

import java.net.Socket;

public abstract class AbsConcurrentServer extends AbstractServer {

    public AbsConcurrentServer(int port) {
        super(port);
        System.out.println("Concurrent Abstract Server initialized ...");
    }

    @Override
    protected void processRequest(Socket client) {
        Thread worker = createWorker(client);
        worker.start();
    }

    protected abstract Thread createWorker(Socket client);
}
