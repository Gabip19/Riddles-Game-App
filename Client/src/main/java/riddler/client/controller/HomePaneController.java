package riddler.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import riddler.domain.User;
import riddler.services.Services;

import java.io.IOException;

public class HomePaneController {
    @FXML
    public Button createChallengeBtn;
    @FXML
    public AnchorPane root;
    private Services srv;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setSrv(Services srv) {
        this.srv = srv;
    }

    public void initialize() {
        createChallengeBtn.setOnAction(param -> showCreateChallengePane());
    }

    private void showCreateChallengePane() {
        FXMLLoader homePaneLoader = new FXMLLoader(getClass().getResource("/create-challenge-pane.fxml"));
        BorderPane parent = (BorderPane) root.getParent();
        try {
            parent.setCenter(homePaneLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CreateChallengeController controller = homePaneLoader.getController();
        controller.setSrv(srv);
        controller.setCurrentUser(currentUser);
    }
}
