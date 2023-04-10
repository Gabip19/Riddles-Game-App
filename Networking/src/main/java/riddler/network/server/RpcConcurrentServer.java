package riddler.network.server;

import riddler.network.rpcprotocol.ClientRpcWorker;
import riddler.services.Services;

import java.net.Socket;

public class RpcConcurrentServer extends AbsConcurrentServer {
    private final Services service;

    public RpcConcurrentServer(int port, Services service) {
        super(port);
        this.service = service;
        System.out.println("Using RpcConcurrentServer ...");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker = new ClientRpcWorker(service, client);
        return new Thread(worker);
    }
}
