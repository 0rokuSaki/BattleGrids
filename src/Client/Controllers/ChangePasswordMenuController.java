package Client.Controllers;

import Client.ClientImpl;
import Client.Controllers.ControllerBase;
import Client.CredentialsManager;
import Shared.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.rmi.RemoteException;

public class ChangePasswordMenuController extends ControllerBase {

    @FXML
    private Button acceptButton;

    @FXML
    private Label infoLabel;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField verifyNewPasswordField;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Set focus on accept button
            acceptButton.requestFocus();

            // Set info label style
            infoLabel.setTextFill(Color.RED);
        });
    }

    @FXML
    void acceptButtonPress(ActionEvent event) throws RemoteException {
        // Get username, old password and new password
        String username = ClientImpl.getInstance().getUsername();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordVerification = verifyNewPasswordField.getText();

        // Change password on server
        Server serverStub = ClientImpl.getInstance().getServerStub();
        String returnMessage = serverStub.handleChangePasswordRequest(username, oldPassword, newPassword, newPasswordVerification);

        // Handle response from server
        if (returnMessage.equals("")) {
            // Delete saved credentials
            CredentialsManager.deleteCredentials();

            // Inform user about success
            infoLabel.setTextFill(Color.GREEN);
            infoLabel.setText("Password changed successfully");
        } else {
            infoLabel.setText(returnMessage);  // Inform user about failure
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "MyProfileMenu.fxml");
    }
}
