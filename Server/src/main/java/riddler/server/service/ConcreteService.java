package riddler.server.service;

import riddler.domain.Challenge;
import riddler.domain.User;
import riddler.domain.validator.Validator;
import riddler.domain.validator.exceptions.InvalidCredentialsException;
import riddler.domain.validator.exceptions.UserValidationException;
import riddler.repository.ChallengeRepository;
import riddler.repository.UserRepository;
import riddler.server.service.comparators.UserComparator;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.net.HttpURLConnection;
import java.net.URL;


public class ConcreteService implements Services {
    private final UserRepository userRepo;
    private final ChallengeRepository challengeRepo;
    private final Map<UUID, ClientObserver> loggedClients;
    private final Validator<User> userValidator;
    private final Validator<Challenge> challengeValidator;
    private final UserComparator userComparator;

    private final int defaultThreadsNum = 5;

    public ConcreteService(UserRepository userRepo, ChallengeRepository challengeRepo, Validator<User> userValidator, Validator<Challenge> challengeValidator, UserComparator userComparator) {
        this.userRepo = userRepo;
        this.challengeRepo = challengeRepo;
        this.userValidator = userValidator;
        this.challengeValidator = challengeValidator;
        this.userComparator = userComparator;
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
    public synchronized User attemptSignUp(User user, ClientObserver client) {
        userValidator.validate(user);

        if (!emailIsAvailable(user.getEmail()))
            throw new UserValidationException("An account with the given email already exists.");

        userRepo.add(user);

        return user;
    }

    private boolean emailIsAvailable(String email) {
        return userRepo.findByEmail(email) == null;
    }

    public synchronized ArrayList<User> getUsers() {
        return new ArrayList<>( (Collection<User>) userRepo.findAll());
    }

    @Override
    public synchronized ArrayList<User> getTopUsers(int topNumber) {
        PriorityQueue<User> topUsers = getTopUsers(topNumber, userComparator);

        ArrayList<User> topUsersArray = new ArrayList<>();
        while (!topUsers.isEmpty()) {
            topUsersArray.add(topUsers.poll());
        }
        Collections.reverse(topUsersArray);

        return topUsersArray;
    }

    @Override
    public synchronized void getRiddle() {
        URL url = null;
        try {
//            url = new URL("https://api.api-ninjas.com/v1/riddles");
            url = new URL("https://riddles-api.vercel.app/random");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                System.out.println("FAIL API");
            } else {
                StringBuilder infoString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    infoString.append(scanner.nextLine());
                }

                scanner.close();
                System.out.println(infoString);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addChallenge(Challenge challenge) {
        challengeValidator.validate(challenge);

        User challengeOwner = challenge.getAuthor();
        challengeOwner.removeBadges(challenge.getBadgesPrizePool());
        challengeOwner.removeTokens(challenge.getTokensPrizePool());

        userRepo.update(challengeOwner, challengeOwner.getId());
        challengeRepo.add(challenge);
    }

    private PriorityQueue<User> getTopUsers(int topNumber, UserComparator comparator) {
        ArrayList<User> users = getUsers();

        topNumber = Math.min(topNumber, users.size());

        if (topNumber < 1) {
            return new PriorityQueue<>();
        }

        PriorityQueue<User> topUsers = new PriorityQueue<>(topNumber, comparator);

        // Add the first topNumber values to the queue
        Iterator<User> iterator = users.iterator();
        for (int i = 0; i < topNumber && iterator.hasNext(); i++) {
            topUsers.offer(iterator.next());
        }

        // Iterate over the rest of the values and add them to the queue if they are larger than the smallest element
        while (iterator.hasNext()) {
            User currentUser = iterator.next();
            if (comparator.compare(topUsers.peek(), currentUser) < 0) {
                topUsers.poll();
                topUsers.offer(currentUser);
            }
        }

        return topUsers;
    }
}
