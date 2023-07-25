package Client.Controllers;


import Client.Controllers.ControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MyProfileMenuController extends ControllerBase {

    @FXML
    void myFriendsButtonPressed(ActionEvent event) {

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
