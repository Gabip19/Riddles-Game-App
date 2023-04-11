package riddler.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;

public class TopUserCell {
    @FXML
    public Label rankLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label badgesLabel;
    @FXML
    public Label tokensLabel;
    public HBox root;

    public static final URL fxmlLocation = TopUserCell.class.getResource("/top-user-cell.fxml");

    public TopUserCell() {
        super();
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setRank(String text) {
        rankLabel.setText(text);
    }

    void setName(String text) {
        nameLabel.setText(text);
    }

    void setBadges(String text) {
        badgesLabel.setText(text);
    }

    void setTokens(String text) {
        tokensLabel.setText(text);
    }

    public HBox getRoot() {
        return root;
    }
}
