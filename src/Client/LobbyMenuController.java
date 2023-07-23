package Client;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LobbyMenuController extends ControllerBase {

    @FXML
    void playGamesButtonPress(ActionEvent event) {

    }

    @FXML
    void friendsButtonPress(ActionEvent event) {

    }

    @FXML
    void myProfileButtonPress(ActionEvent event) {

    }

    @FXML
    void logOutButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
