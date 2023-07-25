package Client;

import Shared.Server;
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

    private final ServerStubHolder serverStubHolder;

    private final CredentialsManager credentialsManager;

    private boolean credentialsLoadedFromFile;

    public LoginMenuController() {
        serverStubHolder = ServerStubHolder.getInstance();
        credentialsManager = new CredentialsManager("./credentials");
        credentialsLoadedFromFile = false;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Hide login error label
            loginErrLabel.setText("");

            // Set focus on login button
            loginButton.requestFocus();

            // Load credentials from file (if applicable)
            Credentials credentials = credentialsManager.loadCredentialsFromFile();
            if (credentials != null) {
                usernameField.setText(credentials.getUsername());
                passwordField.setText(credentials.getPasswordHash());
                rememberMeCheckBox.setSelected(true);

                credentialsLoadedFromFile = true;
            }
        });
    }

    @FXML
    void loginButtonPress(ActionEvent event) throws IOException {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Login to server
        Server serverStub = serverStubHolder.getServerStub();
        String returnMessage = serverStub.handleLoginRequest(username, password);

        // Handle response from server
        if (returnMessage.equals("")) {
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                credentialsManager.saveCredentialsToFile(username, password);
            } else if (credentialsLoadedFromFile) {
                credentialsManager.deleteCredentialsFile();
            }
            changeScene(event, "fxml/LobbyMenu.fxml");
        } else {
            loginErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
