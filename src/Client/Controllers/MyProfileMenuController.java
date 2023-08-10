package Client.Controllers;

import Client.ClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MyProfileMenuController extends ControllerBase {

    @FXML
    private Label userLabel;

    @FXML
    void initialize() {
        userLabel.setText("User: " + ClientImpl.getInstance().getUsername());
    }

    @FXML
    void changePasswordButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "ChangePasswordMenu.fxml");
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        changeScene(event, "LobbyMenu.fxml");
    }
}
