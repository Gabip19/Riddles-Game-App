package riddler.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import riddler.domain.User;
import riddler.domain.validator.exceptions.InvalidCredentialsException;

import java.io.IOException;


public class LoginController extends GuiController {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginBtn;
    @FXML
    public Hyperlink switchToSignUpBtn;

    private Parent mainWindowRoot;
    private MainWindowController mainController;


    public void setMainWindowRoot(Parent mainWindowRoot) {
        this.mainWindowRoot = mainWindowRoot;
    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        loginBtn.setOnAction(param -> login());
        switchToSignUpBtn.setOnAction(param -> switchToSignUpScene());
    }

    private void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            return;
        }

        try {
            System.out.println("login acum");
            currentUser = srv.attemptLogin(new User("", "", email, password), mainController);
            System.out.println("User with email: " + currentUser.getEmail() + " logged in.");
        } catch (InvalidCredentialsException e) {
            emailField.setStyle("-fx-border-width: 2px;-fx-border-color: red");
            passwordField.setStyle("-fx-border-width: 2px;-fx-border-color: red");
            return;
        }

        openMainStage();
        closeStage();
    }

    private void switchToSignUpScene() {
        openSignUpScene();
    }

    private void openSignUpScene() {
        FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/signup.fxml"));

        Scene scene;
        try {
            scene = new Scene(signupLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SignUpController controller = signupLoader.getController();
        controller.setMainController(mainController);
        controller.setMainWindowRoot(mainWindowRoot);
        controller.setLoginWindowRoot(emailField.getScene().getRoot());

        var currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.getScene().setRoot(new AnchorPane());
        currentStage.setTitle("Sign Up");
        currentStage.setScene(scene);
    }

    private void closeStage() {
        var currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.getScene().setRoot(new AnchorPane());
        currentStage.close();
    }

    private void openMainStage() {
        Scene scene = new Scene(mainWindowRoot);

        mainController.setLoginWindowRoot(emailField.getScene().getRoot());
        mainController.load();

        Stage stage = new Stage();
        stage.setOnCloseRequest(param -> mainController.logout());

        stage.setTitle("Riddler");
        stage.setScene(scene);
        stage.show();
    }

}
