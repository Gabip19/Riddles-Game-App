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

public class ClientRpcWorker implements Runnable, ClientObserver {
    private final Services service;
    private final Socket connection;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private volatile boolean connected = false;

    public ClientRpcWorker(Services service, Socket connection) {
        this.service = service;
        this.connection = connection;

        try {
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(connection.getInputStream());
            connected = true;
            System.out.println("Worker initializat cu succes.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Worker incepe executia.");
        while (connected) {
            try {
                Object request = inputStream.readObject();
                System.out.println("Worker a citit.");

                Response response = handleRequest((Request) request);
                System.out.println("Worker handled cererea.");

                if (response != null) {
                    sendResponse(response);
                    System.out.println("Worker a trimis raspus." + response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Eroare la sleep.");
                e.printStackTrace();
            }
        }
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error " + e.getMessage());
        }
    }

    private final Response OK_RESPONSE = new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request) {
        if (request.type() == RequestType.LOGIN) {
            return handleLogin(request);
        }
        if (request.type() == RequestType.LOGOUT) {
            return handleLogout(request);
        }
        return null;
    }

    private Response handleLogin(Request request) {
        System.out.println("Login request ...");
        UserDTO requestUserDTO = (UserDTO) request.data();
        User user = DTOUtils.getFromDTO(requestUserDTO);

        try {
            User loggedUser = service.attemptLogin(user, this);
            UserDTO userDTO = DTOUtils.getDTO(loggedUser);

            return new Response.Builder().type(ResponseType.OK).data(userDTO).build();

        } catch (InvalidCredentialsException e) {
            System.out.println("parola proasta");
            System.out.println(e.getMessage());
            connected = false;

            return new Response.Builder()
                    .type(ResponseType.INVALID_CREDENTIALS)
                    .data(e.getMessage())
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connected = false;

            return new Response.Builder()
                    .type(ResponseType.ERROR)
                    .data(e.getMessage())
                    .build();
        }
    }

    private Response handleLogout(Request request) {
        System.out.println("Logout request ...");
        UserDTO userDTO = (UserDTO) request.data();
        User user = DTOUtils.getFromDTO(userDTO);

        service.logout(user);
        connected = false;
        return OK_RESPONSE;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("[WORKER] Sending response " + response.type());
        outputStream.writeObject(response);
        outputStream.flush();
    }

}