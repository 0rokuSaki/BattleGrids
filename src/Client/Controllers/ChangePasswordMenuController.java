package Client.Controllers;

import Client.ClientModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

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
        });
    }

    @FXML
    void acceptButtonPress(ActionEvent event) {
        // Get username, old password and new password
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordVerification = verifyNewPasswordField.getText();

        // Change password on server
        String returnMessage = ClientModel.getInstance().changePassword(oldPassword, newPassword, newPasswordVerification);

        // Handle response from server
        if (returnMessage.equals("")) {
            acceptButton.setDisable(true);

            // Delete saved credentials
            ClientModel.deleteCredentials();

            // Inform user about success
            infoLabel.setTextFill(Color.GREEN);
            infoLabel.setText("Password changed successfully");
        } else {
            // Inform user about failure
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText(returnMessage);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "MyProfileMenu.fxml");
    }
}
