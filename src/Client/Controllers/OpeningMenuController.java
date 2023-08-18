package Client.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class OpeningMenuController extends ControllerBase {

    @FXML
    void loginButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "LoginMenu.fxml");
    }

    @FXML
    void registerButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "RegistrationMenu.fxml");
    }

    @FXML
    void exitButtonPress(ActionEvent event) {
        Platform.exit();
    }
}
