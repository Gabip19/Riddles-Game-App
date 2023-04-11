package riddler.network.rpcprotocol;


import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;
import riddler.domain.validator.exceptions.*;
import riddler.network.dto.DTOUtils;
import riddler.network.dto.UserDTO;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public User attemptSignUp(User user, ClientObserver client) {
        initializeConnection();
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request signupRequest = new Request.Builder()
                .type(RequestType.SIGN_UP)
                .data(userDTO)
                .build();
        sendRequest(signupRequest);

        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            this.client = client;
            return DTOUtils.getFromDTO((UserDTO) response.data());

        } else if (response.type() == ResponseType.INVALID_USER_DATA) {
            closeConnection();
            String err = response.data().toString();
            throw new UserValidationException(err);

        } else if (response.type() == ResponseType.ERROR) {
            closeConnection();
            String err = response.data().toString();
            throw new RuntimeException(err);
        }

        return null;
    }

    @Override
    public ArrayList<User> getTopUsers(int topNumber) {
        Request getTopRequest = new Request.Builder()
                .type(RequestType.GET_TOP_USERS)
                .data(topNumber)
                .build();
        sendRequest(getTopRequest);

        Response response = readResponse();

        if (response.type() == ResponseType.GET_TOP_USERS) {
            UserDTO[] userDTOS = (UserDTO[]) response.data();
            User[] users = DTOUtils.getFromDTO(userDTOS);
            return new ArrayList<>(List.of(users));
        } else if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }

        return new ArrayList<>();
    }

    @Override
    public Challenge getRiddle() {
        Request riddleRequest = new Request.Builder().type(RequestType.GET_RIDDLE).build();
        sendRequest(riddleRequest);

        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            return (Challenge) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }

        return null;
    }

    @Override
    public void addChallenge(Challenge challenge) {
        Request addRequest = new Request.Builder()
                .type(RequestType.ADD_CHALLENGE)
                .data(challenge)
                .build();
        sendRequest(addRequest);

        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            System.out.println("Added a challenge.");
        } else if (response.type() == ResponseType.INVALID_CHALLENGE_DATA) {
            String err = response.data().toString();
            throw new ChallengeValidationException(err);
        } else if (response.type() == ResponseType.ERROR) {
            closeConnection();
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
    }

    @Override
    public void sendSubmission(Submission submission) {
        Request submissionRequest = new Request.Builder()
                .type(RequestType.SEND_SUBMISSION)
                .data(submission)
                .build();
        sendRequest(submissionRequest);

        Response response = readResponse();
        if (response.type() == ResponseType.INVALID_SUBMISSION_ANSWER) {
            String err = response.data().toString();
            throw new InvalidSubmissionAnswerException(err);
        } else if (response.type() == ResponseType.NO_ATTEMPTS_LEFT) {
            String err = response.data().toString();
            throw new NoAttemptsLeftException(err);
        } else if (response.type() == ResponseType.ERROR) {
            closeConnection();
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

                    if (response.type() == ResponseType.LOGOUT) {
                        connected = false;
                    }

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
                    System.out.println("[READER] Error: " + e.getMessage());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}
