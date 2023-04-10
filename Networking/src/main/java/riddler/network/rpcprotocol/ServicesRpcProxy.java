package riddler.network.rpcprotocol;


import riddler.domain.User;
import riddler.domain.validator.exceptions.InvalidCredentialsException;
import riddler.network.dto.DTOUtils;
import riddler.network.dto.UserDTO;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements Services {
    private final String host;
    private final int port;

    private ClientObserver client;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final BlockingQueue<Response> responses;
    private Socket connection;

    private volatile boolean connected;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
    }

    @Override
    public User attemptLogin(User user, ClientObserver client) {
        System.out.println("Login in proxy.");
        initializeConnection();
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request loginRequest = new Request.Builder()
                .type(RequestType.LOGIN)
                .data(userDTO)
                .build();
        sendRequest(loginRequest);
        System.out.println("Request trimis.");

        Response response = readResponse();
        System.out.println("Raspuns citit.");

        if (response.type() == ResponseType.OK) {
            this.client = client;
            return DTOUtils.getFromDTO((UserDTO) response.data());

        } else if (response.type() == ResponseType.INVALID_CREDENTIALS) {
            closeConnection();
            String err = response.data().toString();
            throw new InvalidCredentialsException(err);

        } else if (response.type() == ResponseType.ERROR) {
            closeConnection();
            String err = response.data().toString();
            throw new RuntimeException(err);
        }

        return null;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(connection.getInputStream());
            connected = true;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Conexiune pornita.");
    }

    private void closeConnection() {
        connected = false;
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader() {
        Thread readerThread = new Thread(new ReaderThread());
        System.out.println("Pornesc reader.");
        readerThread.start();
        System.out.println("Reader pornit.");
    }

    @Override
    public void logout(User user) {
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request logoutRequest = new Request.Builder()
                .type(RequestType.LOGOUT)
                .data(userDTO)
                .build();
        sendRequest(logoutRequest);

        Response response = readResponse();
        closeConnection();
        System.out.println("Conexiune inchisa.");

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
    }

    private boolean isUpdate(Response response) {
        return false;
    }

    private void handleUpdate(Response response) {
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (connected) {
                try {
                    Response response = (Response) inputStream.readObject();
                    System.out.println("Received a response " + response.type());

                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            responses.put(response);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("[READER] Error: " + e.getMessage());
                }
            }
        }
    }
}
