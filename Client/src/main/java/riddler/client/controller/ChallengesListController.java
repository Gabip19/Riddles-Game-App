package riddler.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import riddler.client.gui.ChallengeListCell;
import riddler.domain.Challenge;

public class ChallengesListController extends GuiController {
    @FXML
    public ListView<Challenge> challengesListView;
    private final ObservableList<Challenge> challenges = FXCollections.observableArrayList();

    public void initialize() {
        challengesListView.setItems(challenges);
        challengesListView.setCellFactory(param -> new ChallengeListCell());
        challenges.setAll(srv.getAllChallenges());
    }
}
