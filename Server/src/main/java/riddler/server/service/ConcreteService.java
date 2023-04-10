package riddler.server.service;


import riddler.domain.User;
import riddler.domain.validator.Validator;
import riddler.domain.validator.exceptions.InvalidCredentialsException;
import riddler.domain.validator.exceptions.UserValidationException;
import riddler.repository.UserRepository;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConcreteService implements Services {
    private final UserRepository userRepo;
    private final Map<UUID, ClientObserver> loggedClients;
    private final Validator<User> userValidator;

    private final int defaultThreadsNum = 5;

    public ConcreteService(UserRepository userRepo, Validator<User> userValidator) {
        this.userRepo = userRepo;
        this.userValidator = userValidator;
        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized User attemptLogin(User user, ClientObserver client) {
        System.out.println("Login attempt.");

        String email = user.getEmail();
        String password = user.getPassword();
        User loginUser = userRepo.findByEmail(email);

        if (loginUser != null) {
            if (loggedClients.containsKey(loginUser.getId())) {
                throw new InvalidCredentialsException("User already logged in.");
            }
            if (loginUser.getPassword().equals(password)) {
                loggedClients.put(loginUser.getId(), client);
                return loginUser;
            }
        }
        System.out.println("Invalid ceva la login.");
        throw new InvalidCredentialsException("Invalid credentials.");
    }

    public synchronized void logout(User user) {
        loggedClients.remove(user.getId());
    }

    @Override
    public User attemptSignUp(User user, ClientObserver client) {
        userValidator.validate(user);

        if (!emailIsAvailable(user.getEmail()))
            throw new UserValidationException("An account with the given email already exists.");

        userRepo.add(user);

        return user;
    }

    private boolean emailIsAvailable(String email) {
        return userRepo.findByEmail(email) == null;
    }

}
