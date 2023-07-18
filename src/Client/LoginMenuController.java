package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginMenuController extends MenuBase {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Label loginErrLabel;

    @FXML
    public void initialize() {
        usernameField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
        rememberMeCheckBox.setFocusTraversable(false);
    }

    @FXML
    void loginButtonPress(ActionEvent event) {
        System.out.println("Login");
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "OpeningMenu.fxml");
    }
}
