package riddler.client.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;

public class ChallengeCell extends HBox {

    public HBox root;
    public Label titleLabel;
    public Label authorLabel;
    public Label badgesLabel;
    public Label tokensLabel;
    public Label tokensPrizeLabel;
    public static final URL fxmlLocation = TopUserCell.class.getResource("/challenge-view.fxml");

    public ChallengeCell() {
        super();
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setTitle(String text) {
        titleLabel.setText(text);
    }

    void setAuthor(String text) {
        authorLabel.setText(text);
    }

    void setBadges(String text) {
        badgesLabel.setText(text);
    }

    void setTokensLabel(String text) {
        tokensLabel.setText(text);
    }

    void setTokensPrizeLabel(String text) {
        tokensPrizeLabel.setText(text);
    }

    public HBox getRoot() {
        return root;
    }
}
