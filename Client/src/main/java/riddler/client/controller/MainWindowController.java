package riddler.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import riddler.domain.User;
import riddler.services.ClientObserver;
import riddler.services.Services;


public class MainWindowController implements ClientObserver {
    @FXML
    public Button logoutBtn;

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
    }

    public void initialize() {
        logoutBtn.setOnAction(param -> logout());
    }

    void logout() {
        srv.logout(currentUser);
        closeStage();
        openLoginStage();
    }

    private void closeStage() {
        var currentStage = (Stage) logoutBtn.getScene().getWindow();
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
