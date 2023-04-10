package riddler.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import riddler.services.Services;

import java.io.IOException;

public class SignUpController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public Button signUnBtn;
    public Hyperlink switchToLoginBtn;

    private Services srv;
    private Parent loginWindowRoot;
    private LoginController loginController;

    public void setService(Services srv) {
        this.srv = srv;
    }

    public void setLoginWindowRoot(Parent loginWindowRoot) {
        this.loginWindowRoot = loginWindowRoot;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void initialize() {
        signUnBtn.setOnAction(param -> signUp());
        switchToLoginBtn.setOnAction(param -> switchToLoginScene());
    }

    private void signUp() {

    }

    private void switchToLoginScene() {
        openLoginScene();
    }

    private void openLoginScene() {
        Scene scene = new Scene(loginWindowRoot);

        ((Stage) signUnBtn.getScene().getWindow()).setScene(scene);
    }

//    private void closeStage() {
//        var currentStage = (Stage) emailField.getScene().getWindow();
//        currentStage.getScene().setRoot(new AnchorPane());
//        currentStage.close();
//    }
//
//    private void openMainStage() {
//        Scene scene = new Scene(mainWindowRoot);
//
//        mainController.setService(srv);
//        mainController.setLoginController(this);
//        mainController.setLoginWindowRoot(emailField.getScene().getRoot());
//        mainController.setCurrentUser(currentUser);
//
//        Stage stage = new Stage();
//        stage.setOnCloseRequest(param -> {
//            mainController.logout();
//        });
//
//        stage.setTitle("Teledon Menu");
//        stage.setScene(scene);
//        stage.show();
//    }
}
