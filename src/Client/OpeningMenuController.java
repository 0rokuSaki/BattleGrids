package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class OpeningMenuController extends ControllerBase {

    @FXML
    void loginButtonPress(ActionEvent event) throws IOException {
        System.out.println("Login");
        changeScene(event, "fxml/LoginMenu.fxml");
    }

    @FXML
    void registerButtonPress(ActionEvent event) throws IOException {
        System.out.println("Register");
        changeScene(event, "fxml/RegistrationMenu.fxml");
    }

    @FXML
    void playAsGuestButtonPress(ActionEvent event) {
        System.out.println("Play As Guest");
    }

    @FXML
    void exitButtonPress(ActionEvent event) {
        System.out.println("Quit");
        System.exit(0);
    }
}
