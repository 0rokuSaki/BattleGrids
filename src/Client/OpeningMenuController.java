package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class OpeningMenuController extends ControllerBase {

    @FXML
    void loginButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/LoginMenu.fxml");
    }

    @FXML
    void registerButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/RegistrationMenu.fxml");
    }

    @FXML
    void playAsGuestButtonPress(ActionEvent event) {}

    @FXML
    void exitButtonPress(ActionEvent event) {
        System.exit(0);
    }
}
