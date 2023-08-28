/**
 * ClientDriver.java
 *
 * This class holds the main entry point to the program.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August, 2023
 */

package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        if (!ClientModel.getInstance().initialize()) {
            Platform.exit();
        }

        Parent root = FXMLLoader.load(getClass().getResource("fxml/OpeningMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("BattleGrids");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        ClientModel.getInstance().finalize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
