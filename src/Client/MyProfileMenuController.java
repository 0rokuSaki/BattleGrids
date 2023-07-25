package Client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MyProfileMenuController extends ControllerBase {

    @FXML
    void myFriendsButtonPressed(ActionEvent event) {

    }

    @FXML
    void changePasswordButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/ChangePasswordMenu.fxml");
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        changeScene(event, "fxml/LobbyMenu.fxml");
    }
}
