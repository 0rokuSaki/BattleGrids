package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginControllerController extends ControllerBase {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Label loginErrLabel;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Set focus on login button
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loginButton.requestFocus();
            }
        });
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
