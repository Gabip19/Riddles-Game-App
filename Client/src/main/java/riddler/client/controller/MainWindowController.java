package riddler.client.controller;

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
import riddler.domain.User;
import riddler.services.ClientObserver;
import riddler.services.Services;

import java.io.IOException;


public class MainWindowController implements ClientObserver {
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

    private Services srv;
    private Parent loginWindowRoot;
    private LoginController loginController;

    private User currentUser = null;

    public void setLoginWindowRoot(Parent loginWindowRoot) {
        this.loginWindowRoot = loginWindowRoot;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setService(Services service) {
        this.srv = service;
        topUsers.setAll(srv.getTopUsers(50));
    }

    public void initialize() {
        logOutBtn.setOnAction(param -> logout());
        getRiddleBtn.setOnAction(param -> getRiddle());
        homeBtn.setOnAction(param -> showHomePane());

        topUsersListView.setItems(topUsers);
    }

    private void showHomePane() {
        mainBorderPane.setCenter(homeRoot);
    }

    public void load() {
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
        HomePaneController controller = homePaneLoader.getController();
        controller.setSrv(srv);
        controller.setCurrentUser(currentUser);
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

        loginController.setCurrentUser(null);
        System.out.println("A schimbat scena");
        Stage stage = new Stage();
        stage.setTitle("Teledon Login");
        stage.setScene(scene);
        stage.show();
    }
}