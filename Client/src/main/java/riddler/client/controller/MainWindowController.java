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
    public ListView<User> topUsersListView;
    private final ObservableList<User> topUsers = FXCollections.observableArrayList();

    private Parent homeRoot = null;
    private Parent loginWindowRoot;


    public void setLoginWindowRoot(Parent loginWindowRoot) {
        this.loginWindowRoot = loginWindowRoot;
    }

    public void initialize() {
        logOutBtn.setOnAction(param -> logout());
        getRiddleBtn.setOnAction(param -> getRiddle());
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
    }

    private void loadHomePane() {
        FXMLLoader homePaneLoader = new FXMLLoader(getClass().getResource("/home-pane.fxml"));
        try {
            homeRoot = homePaneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getRiddle() {
        srv.getRiddle();
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

    @Override
    public void updateTop(ArrayList<User> topUsers) {
        System.out.println("Am primit notificare update top users.");
        Platform.runLater(() -> this.topUsers.setAll(topUsers));
    }
}
