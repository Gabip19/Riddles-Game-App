package riddler.services;


import riddler.domain.User;

public interface Services {
    User attemptLogin(User user, ClientObserver client);
    void logout(User user);
    User attemptSignUp(User user, ClientObserver client);
}
