package riddler.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HomePaneController extends GuiController {
    @FXML
    public Button createChallengeBtn;
    @FXML
    public AnchorPane root;


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
    }
}
