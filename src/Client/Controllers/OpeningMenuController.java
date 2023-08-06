package Client.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class OpeningMenuController extends ControllerBase {

    @FXML
    void loginButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "LoginMenu.fxml");
    }

    @FXML
    void registerButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "RegistrationMenu.fxml");
    }

    @FXML
    void playAsGuestButtonPress(ActionEvent event) {}

    @FXML
    void exitButtonPress(ActionEvent event) {
        Platform.exit();
    }
}
