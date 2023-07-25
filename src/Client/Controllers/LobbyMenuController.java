package Client.Controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LobbyMenuController extends ControllerBase {

    @FXML
    void playGamesButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "GamesMenu.fxml");
    }

    @FXML
    void myProfileButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "MyProfileMenu.fxml");
    }

    @FXML
    void logOutButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "OpeningMenu.fxml");
    }
}
