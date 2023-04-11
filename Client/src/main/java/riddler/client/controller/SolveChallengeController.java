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
        } catch (NoAttemptsLeftException e) {
            System.out.println("!!!!! NU MAI AI INCERCARI !!!!! " + e.getMessage());
        }
    }
}
