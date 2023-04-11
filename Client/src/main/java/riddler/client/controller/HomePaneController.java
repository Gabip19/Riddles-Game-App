package riddler.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HomePaneController extends GuiController {
    @FXML
    public Button createChallengeBtn;
    @FXML
    public AnchorPane root;
    @FXML
    public Button randomChallengeBtn;


    public void initialize() {
        createChallengeBtn.setOnAction(param -> showCreateChallengePane());
        randomChallengeBtn.setOnAction(param -> showRandomChallengePane());
    }

    private void showCreateChallengePane() {
        FXMLLoader homePaneLoader = new FXMLLoader(getClass().getResource("/create-challenge-pane.fxml"));
        BorderPane parent = (BorderPane) root.getParent();
        try {
            parent.setCenter(homePaneLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showRandomChallengePane() {
        FXMLLoader challengePaneLoader = new FXMLLoader(getClass().getResource("/solve-challenge-pane.fxml"));
        BorderPane parent = (BorderPane) root.getParent();
        Parent randomChallengePaneRoot;
        try {
            randomChallengePaneRoot = challengePaneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SolveChallengeController controller = challengePaneLoader.getController();
        controller.setChallenge(srv.getRiddle());
        parent.setCenter(randomChallengePaneRoot);
    }
}
