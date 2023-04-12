package riddler.network.rpcprotocol;


import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;
import riddler.domain.validator.exceptions.*;
import riddler.network.dto.ChallengeDTO;
import riddler.network.dto.ChallengeUserDTO;
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

    private Response handleRequest(Request request) {
        if (request.type() == RequestType.LOGIN) {
            return handleLogin(request);
        }
        if (request.type() == RequestType.LOGOUT) {
            return handleLogout(request);
        }
        if (request.type() == RequestType.SIGN_UP) {
            return handleSignUp(request);
        }
        if (request.type() == RequestType.GET_TOP_USERS) {
            return handleTopUsers(request);
        }
        if (request.type() == RequestType.GET_RIDDLE) {
            return handleGetRiddle(request);
        }
        if (request.type() == RequestType.ADD_CHALLENGE) {
            return handleAddChallenge(request);
        }
        if (request.type() == RequestType.SEND_SUBMISSION) {
            return handleSendSubmission(request);
        }
        if (request.type() == RequestType.GET_CHALLENGES) {
            return handleGetChallenges(request);
        }
        if (request.type() == RequestType.GET_NUMBER_OF_ATTEMPTS_LEFT) {
            return handleNumberOfAttempts(request);
        }
        return null;
    }

    private Response handleNumberOfAttempts(Request request) {
        ChallengeUserDTO dto = (ChallengeUserDTO) request.data();
        User user = DTOUtils.getFromDTO(dto.getUserDTO());
        Challenge challenge = DTOUtils.getFromDTO(dto.getChallengeDTO());

        int number = service.getNumberOfAttemptsLeft(user, challenge);
        return new Response.Builder().type(ResponseType.OK).data(number).build();
    }

    private Response handleGetChallenges(Request request) {
        List<Challenge> challenges = service.getAllChallenges();
        ChallengeDTO[] challengesDTO = DTOUtils.getDTO(challenges.toArray(new Challenge[0]));
        return new Response.Builder().type(ResponseType.GET_CHALLENGES).data(challengesDTO).build();
    }

    private Response handleAddChallenge(Request request) {
        try {
            service.addChallenge((Challenge) request.data());
            return new Response.Builder().type(ResponseType.OK).build();

        } catch (ChallengeValidationException e) {
            System.out.println(e.getMessage());
            return new Response.Builder()
                    .type(ResponseType.INVALID_CHALLENGE_DATA)
                    .data(e.getMessage()).build();
        }
    }

    private Response handleSendSubmission(Request request) {
        Submission submission = (Submission) request.data();
        try {
            service.sendSubmission(submission);
            return new Response.Builder().type(ResponseType.OK).build();

        } catch (InvalidSubmissionAnswerException e) {
            System.out.println(e.getMessage());
            return new Response.Builder().type(ResponseType.INVALID_SUBMISSION_ANSWER)
                    .data(e.getMessage()).build();

        } catch (NoAttemptsLeftException e) {
            System.out.println(e.getMessage());
            return new Response.Builder().type(ResponseType.NO_ATTEMPTS_LEFT)
                    .data(e.getMessage()).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR)
                    .data(e.getMessage()).build();
        }
    }

    private Response handleGetRiddle(Request request) {
        System.out.println("Get riddle request ...");

        try {
            Challenge riddle = service.getRiddle();
            return new Response.Builder()
                    .type(ResponseType.OK)
                    .data(riddle)
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
        return new Response.Builder().type(ResponseType.LOGOUT).build();
    }

    private Response handleSignUp(Request request) {
        System.out.println("Sign up request ...");
        UserDTO requestUserDTO = (UserDTO) request.data();
        User user = DTOUtils.getFromDTO(requestUserDTO);

        try {
            User loggedUser = service.attemptSignUp(user, this);
            UserDTO userDTO = DTOUtils.getDTO(loggedUser);

            return new Response.Builder().type(ResponseType.OK).data(userDTO).build();

        } catch (UserValidationException e) {
            System.out.println("Date invalide la user.");
            System.out.println(e.getMessage());
            connected = false;

            return new Response.Builder()
                    .type(ResponseType.INVALID_USER_DATA)
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

    private Response handleTopUsers(Request request) {
        System.out.println("Load top users request ...");
        List<User> users = service.getTopUsers((Integer) request.data());
        UserDTO[] userDTOS = DTOUtils.getDTO(users.toArray(new User[0]));
        return new Response.Builder().type(ResponseType.GET_TOP_USERS).data(userDTOS).build();
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("[WORKER] Sending response " + response.type());
        outputStream.writeObject(response);
        outputStream.flush();
    }

    @Override
    public void updateTop(ArrayList<User> topUsers) {
        UserDTO[] userDTOS = DTOUtils.getDTO(topUsers.toArray(new User[0]));
        Response notification = new Response.Builder().type(ResponseType.TOP_UPDATE)
                .data(userDTOS).build();
        try {
            sendResponse(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
