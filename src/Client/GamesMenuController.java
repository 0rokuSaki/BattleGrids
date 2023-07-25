package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class GamesMenuController extends ControllerBase {

    @FXML
    private GridPane gamesGrid;

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/LobbyMenu.fxml");
    }
}
