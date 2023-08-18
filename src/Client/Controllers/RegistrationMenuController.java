package Client.Controllers;

import Client.ClientModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

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
        Platform.runLater(() -> registerButton.requestFocus());
    }

    @FXML
    void registerButtonPress(ActionEvent event) {
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
                ClientModel.saveCredentials(username, password);
            }
            changeScene(((Node) event.getSource()).getScene(), "LobbyMenu.fxml");
        } else {  // Registration failed
            regErrLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "OpeningMenu.fxml");
    }
}
