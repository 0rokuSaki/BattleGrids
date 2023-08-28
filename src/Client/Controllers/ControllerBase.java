package Client.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ControllerBase class provides utility methods for handling scene changes in a JavaFX application.
 * It includes a method to transition between different FXML-based scenes seamlessly.
 *
 * This class is intended to be extended by other controller classes to reuse scene change functionality.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August, 2023
 */
public class ControllerBase {

    /**
     * Changes the current scene to the one specified by the given FXML file.
     * The previous scene and the name of the FXML file to transition to are provided as parameters.
     *
     * @param prevScene The previous scene from which the transition is initiated.
     * @param fxmlName The name of the FXML file (including path) for the new scene.
     */
    protected void changeScene(Scene prevScene, String fxmlName) {
        fxmlName = "../fxml/" + fxmlName;

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlName));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Stage stage = (Stage) prevScene.getWindow();

        final double prevWidth = prevScene.getWidth();
        final double prevHeight = prevScene.getHeight();

        Scene newScene = new Scene(root, prevWidth, prevHeight);
        stage.setScene(newScene);

        stage.show();
    }
}
