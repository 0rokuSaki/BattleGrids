package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

import java.io.IOException;

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
    void acceptButtonPress(ActionEvent event) {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordVerify = verifyNewPasswordField.getText();
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        changeScene(event, "fxml/MyProfileMenu.fxml");
    }
}
