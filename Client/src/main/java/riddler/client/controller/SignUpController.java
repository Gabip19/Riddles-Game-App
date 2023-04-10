package riddler.client.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import riddler.domain.User;
import riddler.domain.validator.exceptions.UserValidationException;


public class SignUpController extends GuiController {
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public Button signUnBtn;
    public Hyperlink switchToLoginBtn;
    public Label errorLabel;

    private Parent loginWindowRoot;
    private Parent mainWindowRoot;
    private MainWindowController mainController;


    public void setLoginWindowRoot(Parent loginWindowRoot) {
        this.loginWindowRoot = loginWindowRoot;
    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    public void setMainWindowRoot(Parent mainWindowRoot) {
        this.mainWindowRoot = mainWindowRoot;
    }


    public void initialize() {
        signUnBtn.setOnAction(param -> signUp());
        switchToLoginBtn.setOnAction(param -> openLoginScene());
    }

    private void signUp() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (hasEmptyFields()) {
            return;
        }

        try {
            currentUser = srv.attemptSignUp(new User(firstName, lastName, email, password), mainController);
        } catch (UserValidationException e) {
            errorLabel.setText(e.getMessage());
            return;
        }

        openMainStage();
        closeStage();
    }

    private boolean hasEmptyFields() {
        return lastNameField.getText().equals("") ||
                firstNameField.getText().equals("") ||
                emailField.getText().equals("") ||
                passwordField.getText().equals("");
    }

    private void openLoginScene() {
        Scene scene = new Scene(loginWindowRoot);
        ((Stage) signUnBtn.getScene().getWindow()).setScene(scene);
    }

    private void closeStage() {
        var currentStage = (Stage) emailField.getScene().getWindow();
        currentStage.getScene().setRoot(new AnchorPane());
        currentStage.close();
    }

    private void openMainStage() {
        Scene scene = new Scene(mainWindowRoot);

        mainController.setLoginWindowRoot(loginWindowRoot);
        mainController.load();

        Stage stage = new Stage();
        stage.setOnCloseRequest(param -> {
            mainController.logout();
        });

        stage.setTitle("Teledon Menu");
        stage.setScene(scene);
        stage.show();
    }
}
