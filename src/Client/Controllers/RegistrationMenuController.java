package Client.Controllers;

import Client.ClientModel;
import Client.CredentialsManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;

public class RegistrationMenuController extends ControllerBase {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField verifyPasswordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Label regErrLabel;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        // Hide registration error label
        regErrLabel.setText("");

        // Set focus on register button
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                registerButton.requestFocus();
            }
        });
    }

    @FXML
    void registerButtonPress(ActionEvent event) throws IOException {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordVerification = verifyPasswordField.getText();

        // Register to server
        String returnMessage = ClientModel.getInstance().register(username, password, passwordVerification);

        // Handle response from server
        if (returnMessage.equals("")) {  // Registration successful
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                CredentialsManager.saveCredentials(username, password);
            }
            changeScene(((Node) event.getSource()).getScene(), "LobbyMenu.fxml");
        } else {  // Registration failed
            regErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(((Node) event.getSource()).getScene(), "OpeningMenu.fxml");
    }
}
