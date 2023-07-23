package Client;

import Shared.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private final ServerStubHolder serverStubHolder;

    private final CredentialsManager credentialsManager;

    public RegistrationMenuController() {
        serverStubHolder = ServerStubHolder.getInstance();
        credentialsManager = new CredentialsManager("./credentials");
    }

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
    void registerButtonPress(ActionEvent event) {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String verifyPassword = verifyPasswordField.getText();

        // Validate username & password fields
        if (verifyPassword.equals("") || password.equals("") || username.equals("")) {
            regErrLabel.setText("Invalid username and/or password");
            return;
        } else if (!password.equals(verifyPassword)) {
            regErrLabel.setText("Passwords do not match");
            return;
        }

        // Generate password hash
        String passwordHash;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            passwordHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException ignored) {
            passwordHash = "";
        }

        // Register to server
        Server.ReturnCode rc = Server.ReturnCode.NO_ERROR;
        boolean registerSuccess = false;
        try {
            rc = serverStubHolder.getServerStub().handleRegistrationRequest(username, passwordHash);
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
        } else if (rc == Server.ReturnCode.USER_ALREADY_EXISTS) {
            regErrLabel.setText("User already exists");
        } else {
            regErrLabel.setText("Failed to register");
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
