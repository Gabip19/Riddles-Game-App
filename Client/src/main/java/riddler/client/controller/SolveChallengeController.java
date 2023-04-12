package riddler.client.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;
import riddler.domain.validator.exceptions.InvalidSubmissionAnswerException;
import riddler.domain.validator.exceptions.NoAttemptsLeftException;

import java.time.LocalDateTime;

public class SolveChallengeController extends GuiController {
    public Label titleLabel;
    public Label authorLabel;
    public Label textLabel;
    public TextField answerField;
    public Button submitBtn;
    public Label resultLabel;
    public Label attemptLabel;

    private Challenge challenge;

    public void initialize() {
        submitBtn.setOnAction(param -> submit());
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        load(challenge);
    }

    public void load(Challenge challenge) {
        titleLabel.setText(challenge.getTitle());
        textLabel.setText(challenge.getText());
        resultLabel.setVisible(false);
        User author = challenge.getAuthor();
        if (author != null) {
            authorLabel.setText(author.getFirstName() + " " + author.getLastName());
        }
        updateAttemptsNumberLabel();
        checkIfIsOwner();
    }

    private void updateAttemptsNumberLabel() {
        int numberOfAttemptsLeft = srv.getNumberOfAttemptsLeft(currentUser, this.challenge);
        if (numberOfAttemptsLeft == 0) {
            attemptLabel.setText("No attempts left.");
            attemptLabel.setStyle("-fx-text-fill: red");
            blockInput();
        } else if (challenge.getMaxAttempts() != Challenge.INFINITE_ATTEMPTS) {
            attemptLabel.setText("Attempts left: " + numberOfAttemptsLeft);
        } else {
            attemptLabel.setText("Unlimited attempts");
        }
    }

    private void submit() {
        if (answerField.getText().isBlank()) {
            return;
        }

        Submission submission = new Submission(
                challenge,
                currentUser,
                answerField.getText(),
                LocalDateTime.now()
        );

        try {
            srv.sendSubmission(submission);
            resultLabel.setText("Correct");
            resultLabel.setStyle("-fx-text-fill: #3eea3e");
            resultLabel.setVisible(true);
        } catch (InvalidSubmissionAnswerException e) {
            resultLabel.setText("Wrong");
            resultLabel.setStyle("-fx-text-fill: red");
            resultLabel.setVisible(true);
            updateAttemptsNumberLabel();
        } catch (NoAttemptsLeftException e) {
            resultLabel.setText("No attempts left");
            resultLabel.setStyle("-fx-text-fill: red");
            resultLabel.setVisible(true);
        }
    }

    private void checkIfIsOwner() {
        if (challenge.getAuthor() != null && challenge.getAuthor().equals(currentUser)) {
            blockInput();
            attemptLabel.setText("You can not solve your own challenge.");
            attemptLabel.setStyle("-fx-text-fill: red");
        }
    }

    private void blockInput() {
        answerField.setDisable(true);
        submitBtn.setDisable(true);
    }
}
