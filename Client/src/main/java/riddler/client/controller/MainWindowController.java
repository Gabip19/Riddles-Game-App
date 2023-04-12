package riddler.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import riddler.client.gui.TopUsersListCell;
import riddler.domain.User;
import riddler.services.ClientObserver;

import java.io.IOException;
import java.util.ArrayList;


public class MainWindowController extends GuiController implements ClientObserver {
    @FXML
    public Button logOutBtn;
    @FXML
    public Button getRiddleBtn;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public Button homeBtn;
    @FXML
    public Label tokensLabel;
    @FXML
    public Label badgesLabel;
    @FXML
    public Label firstNameLabel;
    @FXML
    public Label lastNameLabel;
    @FXML
    public ListView<User> topUsersListView;
    private final ObservableList<User> topUsers = FXCollections.observableArrayList();

    private Parent homeRoot = null;
    private Parent loginWindowRoot;


    public void setLoginWindowRoot(Parent loginWindowRoot) {
        this.loginWindowRoot = loginWindowRoot;
    }

    public void initialize() {
        logOutBtn.setOnAction(param -> logout());
        homeBtn.setOnAction(param -> showHomePane());
        initializeTopUsersListView();
    }

    private void initializeTopUsersListView() {
        topUsersListView.setCellFactory(param -> new TopUsersListCell());
        topUsersListView.setItems(topUsers);
    }

    private void showHomePane() {
        mainBorderPane.setCenter(homeRoot);
    }

    public void load() {
        topUsers.setAll(srv.getTopUsers(50));
        loadHomePane();
        mainBorderPane.setCenter(homeRoot);
        firstNameLabel.setText(currentUser.getFirstName());
        lastNameLabel.setText(currentUser.getLastName());
        badgesLabel.setText(String.valueOf(currentUser.getNoBadges()));
        tokensLabel.setText(String.valueOf(currentUser.getNoTokens()));
    }

    private void loadHomePane() {
        FXMLLoader homePaneLoader = new FXMLLoader(getClass().getResource("/home-pane.fxml"));
        try {
            homeRoot = homePaneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void logout() {
        srv.logout(currentUser);
        closeStage();
        openLoginStage();
    }

    private void closeStage() {
        var currentStage = (Stage) logOutBtn.getScene().getWindow();
        currentStage.getScene().setRoot(new AnchorPane());
        currentStage.close();
    }

    private void openLoginStage() {
        System.out.println("Incearca sa schimbe.");
        Scene scene = new Scene(loginWindowRoot);

        System.out.println("A schimbat scena");
        Stage stage = new Stage();
        stage.setTitle("Teledon Login");
        stage.setScene(scene);
        stage.show();
    }

    private void updateCurrentUserData() {
        int index = topUsers.indexOf(currentUser);
        currentUser = topUsers.get(index);
        badgesLabel.setText(String.valueOf(currentUser.getNoBadges()));
        tokensLabel.setText(String.valueOf(currentUser.getNoTokens()));
    }

    @Override
    public void updateTop(ArrayList<User> topUsers) {
        System.out.println("Am primit notificare update top users.");
        Platform.runLater(() -> {
            this.topUsers.setAll(topUsers);
            updateCurrentUserData();
        });
    }
}
