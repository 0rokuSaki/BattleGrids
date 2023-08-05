package Client.Controllers;

import Client.ClientImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;

public class GamesMenuController extends ControllerBase {

    @FXML
    private ComboBox<String> gamesComboBox;

    @FXML
    private Label errLabel;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            String[] gamesList = ClientImpl.getInstance().getGamesList();
            if (gamesList != null) {
                gamesComboBox.setItems(FXCollections.observableArrayList(gamesList));
            } else {
                errLabel.setText("Unable to get games list");
                errLabel.setVisible(true);
            }
        });
    }

    @FXML
    void playButtonPress(ActionEvent event) {
        String gameName = gamesComboBox.getValue();
        if (gameName == null) {
            errLabel.setText("No game selected");
            errLabel.setVisible(true);
            return;
        }

        String returnMessage = ClientImpl.getInstance().playGame(gameName);
        if (returnMessage.equals("")) {
            System.out.println("Playing game");
        } else {
            errLabel.setText(returnMessage);
            errLabel.setVisible(true);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "LobbyMenu.fxml");
    }
}
