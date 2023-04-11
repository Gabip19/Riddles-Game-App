package riddler.client.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import riddler.domain.User;

public class TopUsersListCell extends ListCell<User> {

    public TopUsersListCell() {
        super();
        setStyle("-fx-background-color: rgba(37,36,41,1);");
        setPadding(new Insets(0, 0, 10, 0));
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            TopUserCell cell = new TopUserCell();
            cell.setRank("#" + String.valueOf(getIndex() + 1));
            cell.setName(item.getFirstName() + " " + item.getLastName());
            cell.setBadges(String.valueOf(item.getNoBadges()));
            cell.setTokens(String.valueOf(item.getNoTokens()));

            setText(null);
            setGraphic(cell.getRoot());
        }
    }
}
