package Client;

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

    private final String credentialsFilePath = "./credentials";

    private boolean credentialsLoadedFromFile = false;

    private final ServerStubHolder serverStubHolder = ServerStubHolder.getInstance();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Hide login error label
            loginErrLabel.setVisible(false);

            // Set focus on login button
            loginButton.requestFocus();

            // Load credentials from file (if applicable)
            Credentials credentials = loadCredentialsFromFile();
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
            loginErrLabel.setVisible(true);
            return;
        }

        // Generate password hash (if applicable)
        String passwordHash;
        if (credentialsLoadedFromFile) {
            passwordHash = password;
        } else {
            passwordHash = generatePasswordHash(password);
        }

        // Login to server
        boolean loginSuccess = false;
        try {
             loginSuccess = serverStubHolder.getServerStub().handleLoginRequest(username, passwordHash);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        if (loginSuccess) {
            System.out.println("Login succeeded");

            // Save credentials
            if (rememberMeCheckBox.isSelected()) {
                saveCredentialsToFile(username, passwordHash);
            }

            // TODO: Enter game lobby
            //changeScene(event, "");
        } else {
            loginErrLabel.setText("Incorrect username and/or password");
            loginErrLabel.setVisible(true);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/OpeningMenu.fxml");
    }

    private String generatePasswordHash(String password) {
        String result = "";
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {}

        if (md != null) {
            md.update(password.getBytes());
            byte[] digest = md.digest();
            result = DatatypeConverter.printHexBinary(digest).toUpperCase();
        }

        return result;
    }

    private Credentials loadCredentialsFromFile() {
        try (FileInputStream fileIn = new FileInputStream(credentialsFilePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            return  (Credentials) (objectIn.readObject());
        }
        catch (IOException | ClassNotFoundException ignored) {}
        return null;
    }

    private void saveCredentialsToFile(String username, String passwordHash) {
        try (FileOutputStream fileOut = new FileOutputStream(credentialsFilePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(new Credentials(username, passwordHash));
        } catch (IOException ignored) {}
    }
}
