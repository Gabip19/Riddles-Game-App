package riddler.services;


import riddler.domain.User;

import java.util.ArrayList;

public interface ClientObserver {
    void updateTop(ArrayList<User> topUsers);
}
