package Client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerBase {
    protected void changeScene(ActionEvent event, String fxmlName) throws IOException {
        fxmlName = "../fxml/" + fxmlName;

        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene prevScene = (Scene) ((Node) event.getSource()).getScene();

        final double prevWidth = prevScene.getWidth();
        final double prevHeight = prevScene.getHeight();

        Scene scene = new Scene(root, prevWidth, prevHeight);
        stage.setScene(scene);

        stage.show();
    }
}
