package Client;

import Shared.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private final ServerStubHolder serverStubHolder;

    private final CredentialsManager credentialsManager;

    private boolean credentialsLoadedFromFile;

    public LoginControllerController() {
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

                credentialsLoadedFromFile = true;
            }
        });
    }

    @FXML
    void loginButtonPress(ActionEvent event) {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate username & password fields
        if (password.equals("") || username.equals("")) {
            loginErrLabel.setText("Invalid username and/or password");
            return;
        }

        // Generate password hash (if applicable)
        String passwordHash;
        if (credentialsLoadedFromFile) {
            passwordHash = password;
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                passwordHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            } catch (NoSuchAlgorithmException ignored) {
                passwordHash = "";
            }
        }

        // Login to server
        Server.ReturnCode rc = Server.ReturnCode.NO_ERROR;
        try {
             rc = serverStubHolder.getServerStub().handleLoginRequest(username, passwordHash);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }

        // Handle response from server
        if (rc == Server.ReturnCode.NO_ERROR) {
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                credentialsManager.saveCredentialsToFile(username, passwordHash);
            }

            // TODO: Enter game lobby
            //changeScene(event, "scene.fxml");
        } else if (rc == Server.ReturnCode.INCORRECT_LOGIN_INFO) {
            loginErrLabel.setText("Incorrect username and/or password");
        } else {
            loginErrLabel.setText("Failed to login");
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
