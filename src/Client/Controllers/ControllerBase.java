package Client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerBase {
    protected void changeScene(Scene prevScene, String fxmlName) throws IOException {
        fxmlName = "../fxml/" + fxmlName;

        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Stage stage = (Stage) prevScene.getWindow();

        final double prevWidth = prevScene.getWidth();
        final double prevHeight = prevScene.getHeight();

        Scene newScene = new Scene(root, prevWidth, prevHeight);
        stage.setScene(newScene);

        stage.show();
    }
}
