package Client;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MyProfileMenuController extends ControllerBase {

    @FXML
    void updateUserDetailsButtonPressed(ActionEvent event) {

    }

    @FXML
    void myFriendsButtonPressed(ActionEvent event) {

    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        changeScene(event, "fxml/LobbyMenu.fxml");
    }

}
