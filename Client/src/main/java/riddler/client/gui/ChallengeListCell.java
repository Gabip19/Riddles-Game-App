package riddler.client.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import riddler.domain.Challenge;

public class ChallengeListCell extends ListCell<Challenge> {

    public ChallengeListCell() {
        super();
        setStyle("-fx-background-color: rgba(37,36,41,1); -fx-background-radius: 30px;");
        setPadding(new Insets(0, 0, 10, 0));
    }

    @Override
    protected void updateItem(Challenge item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            ChallengeCell cell = new ChallengeCell();
            cell.setTitle(item.getTitle());
            cell.setAuthor("by " + item.getAuthor().getFirstName());
            cell.setBadges(String.valueOf(item.getBadgesPrizePool()));
            cell.setTokensLabel(String.valueOf(item.getTokensPrizePool()));
            cell.setTokensPrizeLabel(String.valueOf(item.getTokensPrize()));

            setText(null);
            setGraphic(cell.getRoot());
        }
    }
}
