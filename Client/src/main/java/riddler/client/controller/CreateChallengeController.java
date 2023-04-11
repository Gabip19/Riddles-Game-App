package riddler.client.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import riddler.domain.Challenge;
import riddler.domain.validator.exceptions.ChallengeValidationException;

public class CreateChallengeController extends GuiController {

    public TextField titleField;
    public TextArea challengeTextField;
    public TextField answerField;
    public TextField maxAttemptsField;
    public TextField badgesPoolField;
    public TextField tokensPoolField;
    public TextField tokensPrizeField;
    public Button addChallengeBtn;
    public Label resultLabel;


    public void initialize() {
        addChallengeBtn.setOnAction(param -> addChallenge());
        resultLabel.setVisible(false);
    }

    private void addChallenge() {
        try {
            resultLabel.setVisible(false);

            if (hasEmptyFields()) {
                return;
            }

            String title = titleField.getText();
            String text = challengeTextField.getText();
            String answer = answerField.getText();
            int maxAttempts;
            if (maxAttemptsField.getText().equals("no")) {
                maxAttempts = Challenge.INFINITE_ATTEMPTS;
            } else {
                maxAttempts = Integer.parseInt(maxAttemptsField.getText());
            }
            int badgesPrizePool = Integer.parseInt(badgesPoolField.getText());
            int tokensPrizePool = Integer.parseInt(tokensPoolField.getText());
            int tokensPrize = Integer.parseInt(tokensPrizeField.getText());


            Challenge challenge = new Challenge(
                    title,
                    text,
                    answer,
                    currentUser,
                    maxAttempts,
                    badgesPrizePool,
                    tokensPrizePool,
                    tokensPrize
            );

            srv.addChallenge(challenge);
            clearFields();
            resultLabel.setText("Succes");
            resultLabel.setStyle("-fx-text-fill: #3eea3e");
            resultLabel.setVisible(true);
        } catch (NumberFormatException e) {
            resultLabel.setText("Not a number.\n");
            resultLabel.setStyle("-fx-text-fill: red");
            resultLabel.setVisible(true);
        } catch (ChallengeValidationException e) {
            resultLabel.setText(e.getMessage());
            resultLabel.setStyle("-fx-text-fill: red");
            resultLabel.setVisible(true);
        }
    }

    private void clearFields() {
        titleField.clear();
        challengeTextField.clear();
        answerField.clear();
        maxAttemptsField.clear();
        badgesPoolField.clear();
        tokensPoolField.clear();
        tokensPrizeField.clear();
    }

    private boolean hasEmptyFields() {
        return titleField.getText().equals("") ||
                challengeTextField.getText().equals("") ||
                answerField.getText().equals("") ||
                maxAttemptsField.getText().equals("") ||
                badgesPoolField.getText().equals("") ||
                tokensPoolField.getText().equals("") ||
                tokensPrizeField.getText().equals("");
    }

}
