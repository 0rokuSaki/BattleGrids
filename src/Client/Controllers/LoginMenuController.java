package Client.Controllers;

import Client.ClientModel;
import Client.Credentials;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

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
            Credentials credentials = ClientModel.loadCredentials();
            if (credentials != null) {
                usernameField.setText(credentials.getUsername());
                passwordField.setText(credentials.getPassword());
                rememberMeCheckBox.setSelected(true);

                loadedCredentials = true;
            }
        });
    }

    @FXML
    void loginButtonPress(ActionEvent event) {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Login to server
        String returnMessage = ClientModel.getInstance().logIn(username, password);

        // Handle response from server
        if (returnMessage.equals("")) { // Login successful
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                ClientModel.saveCredentials(username, password);
            } else if (loadedCredentials) {
                ClientModel.deleteCredentials();
            }

            // Change to lobby menu
            changeScene(((Node) event.getSource()).getScene(), "LobbyMenu.fxml");
        } else {  // Login failed
            loginErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "OpeningMenu.fxml");
    }
}
