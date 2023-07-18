package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrationMenuController extends MenuBase {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField verifyPasswordField;

    @FXML
    private Label regErrLabel;

    @FXML
    public void initialize() {
        usernameField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
        verifyPasswordField.setFocusTraversable(false);
    }

    @FXML
    void registerButtonPress(ActionEvent event) {
        System.out.println("Register");
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "OpeningMenu.fxml");
    }
}
