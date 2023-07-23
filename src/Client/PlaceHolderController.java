package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class PlaceHolderController extends ControllerBase {

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
