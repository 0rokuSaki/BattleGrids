package Client.Controllers;

import Client.ClientModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class GamesMenuController extends ControllerBase {

    @FXML
    private ComboBox<String> gamesComboBox;

    @FXML
    private Label errLabel;

    @FXML
    private Button playButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            ArrayList<String> gamesList = ClientModel.getInstance().getGamesList();
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

        String returnMessage = ClientModel.getInstance().playGame(gameName);
        if (returnMessage.equals("")) {
            gamesComboBox.setDisable(true);
            playButton.setDisable(true);
            //backButton.setDisable(true);
            errLabel.setText("Waiting for player");
            errLabel.setTextFill(Color.GREEN);
            errLabel.setVisible(true);
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
