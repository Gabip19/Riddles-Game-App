package riddler.client.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import riddler.domain.Challenge;
import riddler.domain.User;

public class SolveChallengeController extends GuiController {
    public Label titleLabel;
    public Label authorLabel;
    public Label textLabel;
    public TextField answerField;
    public Button submitBtn;

    private Challenge challenge;

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        load(challenge);
    }

    public void load(Challenge challenge) {
        titleLabel.setText(challenge.getTitle());
        textLabel.setText(challenge.getText());
        User author = challenge.getAuthor();
        if (author != null) {
            authorLabel.setText(author.getFirstName() + " " + author.getLastName());
        }
    }
}
