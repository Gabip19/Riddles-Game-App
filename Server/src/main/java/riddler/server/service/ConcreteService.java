package riddler.server.service;

import com.google.gson.Gson;
import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;
import riddler.domain.validator.Validator;
import riddler.domain.validator.exceptions.InvalidCredentialsException;
import riddler.domain.validator.exceptions.InvalidSubmissionAnswerException;
import riddler.domain.validator.exceptions.NoAttemptsLeftException;
import riddler.domain.validator.exceptions.UserValidationException;
import riddler.repository.ChallengeRepository;
import riddler.repository.SubmissionRepository;
import riddler.repository.UserRepository;
import riddler.server.service.comparators.UserComparator;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConcreteService implements Services {
    private final UserRepository userRepo;
    private final ChallengeRepository challengeRepo;
    private final SubmissionRepository submissionRepo;
    private final Map<UUID, ClientObserver> loggedClients;
    private final Validator<User> userValidator;
    private final Validator<Challenge> challengeValidator;
    private final UserComparator userComparator;

    private final int defaultThreadsNum = 5;

    public ConcreteService(UserRepository userRepo, ChallengeRepository challengeRepo, SubmissionRepository submissionRepo, Validator<User> userValidator, Validator<Challenge> challengeValidator, UserComparator userComparator) {
        this.userRepo = userRepo;
        this.challengeRepo = challengeRepo;
        this.submissionRepo = submissionRepo;
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

    public ArrayList<User> getUsers() {
        return new ArrayList<>( (Collection<User>) userRepo.findAll());
    }

    @Override
    public ArrayList<User> getTopUsers(int topNumber) {
        PriorityQueue<User> topUsers = getTopUsers(topNumber, userComparator);

        ArrayList<User> topUsersArray = new ArrayList<>();
        while (!topUsers.isEmpty()) {
            topUsersArray.add(topUsers.poll());
        }
        Collections.reverse(topUsersArray);

        return topUsersArray;
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

    @Override
    public synchronized Challenge getRiddle() {
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
                return extractChallenge(infoString.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Challenge extractChallenge(String infoString) {
        var jsonRootObject = new Gson().fromJson(infoString, Map.class);
        String text = (String) jsonRootObject.get("riddle");
        String answer = (String) jsonRootObject.get("answer");
        return new Challenge("Random riddle? Try this one!", text, answer, null, Challenge.INFINITE_ATTEMPTS, 1, 100, 100);
    }

    @Override
    public synchronized void addChallenge(Challenge challenge) {
        challengeValidator.validate(challenge);

        User challengeOwner = challenge.getAuthor();
        challengeOwner.removeBadges(challenge.getBadgesPrizePool());
        challengeOwner.removeTokens(challenge.getTokensPrizePool());

        userRepo.update(challengeOwner, challengeOwner.getId());
        challengeRepo.add(challenge);
        notifyTopUpdate();
    }

    @Override
    public synchronized void sendSubmission(Submission submission) {
        Challenge challenge = challengeRepo.findById(submission.getChallenge().getId());

        if (challenge == null) {
            challengeRepo.add(submission.getChallenge());
            challenge = submission.getChallenge();
        }

        int noAttempts = submissionRepo.getNumberOfAttempts(submission.getUser(), submission.getChallenge());
        if (challenge.getMaxAttempts() != Challenge.INFINITE_ATTEMPTS && noAttempts == challenge.getMaxAttempts()) {
            throw new NoAttemptsLeftException("No attempts left.\n");
        }

        if (solvedChallenge(submission)) {
            rewardUser(submission, noAttempts + 1);
            updatePrizePool(submission.getChallenge(), noAttempts + 1);
            submission.setSolved(true);
            submissionRepo.add(submission);
            notifyTopUpdate();
        } else {
            submissionRepo.add(submission);
            throw new InvalidSubmissionAnswerException("Wrong answer.\n");
        }
    }

    @Override
    public ArrayList<Challenge> getAllChallenges() {
        return new ArrayList<>((Collection<Challenge>) challengeRepo.findChallengesFromUsers());
    }

    private boolean solvedChallenge(Submission submission) {
        String userAnswer = submission.getAnswer().toLowerCase();
        String correctAnswer = submission.getChallenge().getAnswer().toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            return true;
        } else if (correctAnswer.startsWith(userAnswer)) {
            return true;
        } else if (userAnswer.equals(correctAnswer.split(" ")[1])) {
            return true;
        } else {
            int answerNoWords = userAnswer.split(" ").length;
            int correctAnswerNoWords = correctAnswer.split(" ").length;
            return answerNoWords >= (correctAnswerNoWords / 2) && correctAnswer.contains(userAnswer);
        }
    }

    private synchronized void rewardUser(Submission submission, int attemptNum) {
        User user = submission.getUser();
        Challenge challenge = submission.getChallenge();

        if (attemptNum == 1 && challenge.getBadgesPrizePool() > 0) {
            user.addBadges(1);
        } else if (challenge.getTokensPrize() > 0) {
            user.addTokens(challenge.getTokensPrize());
        }

        userRepo.update(user, user.getId());
    }

    private synchronized void updatePrizePool(Challenge challenge, int attemptNum) {
        if (attemptNum == 1 && challenge.getBadgesPrizePool() > 0) {
            challenge.decreaseBadgesPool();
        } else if (challenge.getTokensPrizePool() > 0) {
            challenge.decreaseTokensPool();
        }

        challengeRepo.update(challenge, challenge.getId());
    }

    private void notifyTopUpdate() {
        ArrayList<User> topUsers = getTopUsers(50);

        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNum);

        for (ClientObserver client : loggedClients.values()) {
            executorService.execute(() -> client.updateTop(topUsers));
        }

        executorService.shutdown();
    }

}
