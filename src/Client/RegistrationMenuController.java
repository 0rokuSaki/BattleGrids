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
    void registerButtonPress(ActionEvent event) throws IOException {
        // Get text from fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String verifyPassword = verifyPasswordField.getText();

        // Make sure passwords match
        if (!password.equals(verifyPassword)) {
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
        String returnMessage = serverStubHolder.getServerStub().handleRegistrationRequest(username, passwordHash);

        // Handle response from server
        if (returnMessage.equals("")) {
            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                credentialsManager.saveCredentialsToFile(username, passwordHash);
            }

            // TODO: Enter game lobby
            changeScene(event, "fxml/PlaceHolder.fxml");
        } else {
            regErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }
}
