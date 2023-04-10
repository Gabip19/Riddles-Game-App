package riddler.client.controller;

import riddler.domain.User;
import riddler.services.Services;

public abstract class GuiController {
    protected static Services srv;
    protected static User currentUser;

    public static void setSrv(Services srv) {
        GuiController.srv = srv;
    }
}
