package riddler.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import riddler.client.gui.ChallengeListCell;
import riddler.domain.Challenge;

import java.io.IOException;

public class ChallengesListController extends GuiController {
    @FXML
    public ListView<Challenge> challengesListView;
    private final ObservableList<Challenge> challenges = FXCollections.observableArrayList();
    public AnchorPane root;

    public void initialize() {
        challengesListView.setItems(challenges);
        challengesListView.setCellFactory(param -> new ChallengeListCell(this));
        challengesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        challenges.setAll(srv.getAllChallenges());
    }

    public void showChallengePane(Challenge challenge) {
        FXMLLoader challengePaneLoader = new FXMLLoader(getClass().getResource("/solve-challenge-pane.fxml"));
        BorderPane parent = (BorderPane) root.getParent();
        Parent randomChallengePaneRoot;
        try {
            randomChallengePaneRoot = challengePaneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SolveChallengeController controller = challengePaneLoader.getController();
        controller.setChallenge(challenge);
        parent.setCenter(randomChallengePaneRoot);
    }
}
