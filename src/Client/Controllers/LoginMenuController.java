package Client.Controllers;

import Client.ClientModel;
import Client.Credentials;
import Client.CredentialsManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;

public class LoginMenuController extends ControllerBase {

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

    private boolean loadedCredentials;

    public LoginMenuController() {
        loadedCredentials = false;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Hide login error label
            loginErrLabel.setText("");

            // Set focus on login button
            loginButton.requestFocus();

            // Load credentials from file (if applicable)
            Credentials credentials = CredentialsManager.loadCredentials();
            if (credentials != null) {
                usernameField.setText(credentials.getUsername());
                passwordField.setText(credentials.getPassword());
                rememberMeCheckBox.setSelected(true);

                loadedCredentials = true;
            }
        });
    }

    @FXML
    void loginButtonPress(ActionEvent event) throws IOException {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Login to server
        String returnMessage = ClientModel.getInstance().logIn(username, password);

        // Handle response from server
        if (returnMessage.equals("")) { // Login successful
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                CredentialsManager.saveCredentials(username, password);
            } else if (loadedCredentials) {
                CredentialsManager.deleteCredentials();
            }

            // Change to lobby menu
            changeScene(event, "LobbyMenu.fxml");
        } else {  // Login failed
            loginErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "OpeningMenu.fxml");
    }
}
