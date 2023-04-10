package riddler.client.controller;

import javafx.scene.control.Button;
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


    public void initialize() {
        addChallengeBtn.setOnAction(param -> addChallenge());
    }

    private void addChallenge() {
        try {
            String title = titleField.getText();
            String text = challengeTextField.getText();
            String answer = answerField.getText();
            int maxAttempts = Integer.parseInt(maxAttemptsField.getText());
            int badgesPrizePool = Integer.parseInt(badgesPoolField.getText());
            int tokensPrizePool = Integer.parseInt(tokensPoolField.getText());
            int tokensPrize = Integer.parseInt(tokensPrizeField.getText());

            if (hasEmptyFields()) {
                return;
            }

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
        } catch (NumberFormatException e) {
            System.out.println("Not a number.\n");
        } catch (ChallengeValidationException e) {
            System.out.println(e.getMessage());
        }
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
