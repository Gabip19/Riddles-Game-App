package riddler.services;


import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;

import java.util.ArrayList;

public interface Services {
    User attemptLogin(User user, ClientObserver client);
    void logout(User user);
    User attemptSignUp(User user, ClientObserver client);
    ArrayList<User> getTopUsers(int topNumber);
    Challenge getRiddle();
    void addChallenge(Challenge challenge);
    void sendSubmission(Submission submission);
    ArrayList<Challenge> getAllChallenges();
    int getNumberOfAttemptsLeft(User user, Challenge challenge);
}
