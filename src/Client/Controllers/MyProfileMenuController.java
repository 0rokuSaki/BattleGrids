package Client.Controllers;

import Client.ClientModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;

public class MyProfileMenuController extends ControllerBase {

    @FXML
    private Label userLabel;

    @FXML
    void initialize() {
        userLabel.setText("User: " + ClientModel.getInstance().getUsername());
    }

    @FXML
    void changePasswordButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "ChangePasswordMenu.fxml");
    }

    @FXML
    void backButtonPressed(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "LobbyMenu.fxml");
    }
}
