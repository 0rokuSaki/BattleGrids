package Client.Controllers;

import java.io.IOException;

import Client.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class LobbyMenuController extends ControllerBase {

    @FXML
    void playGamesButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "GamesMenu.fxml");
    }

    @FXML
    void highScoresButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "HighScoresMenu.fxml");
    }

    @FXML
    void myProfileButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "MyProfileMenu.fxml");
    }

    @FXML
    void logOutButtonPress(ActionEvent event) {
        ClientModel.getInstance().logOut();
        changeScene(((Node) event.getSource()).getScene(), "OpeningMenu.fxml");
    }
}
