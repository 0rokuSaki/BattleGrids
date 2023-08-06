package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ClientDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        new Thread(ClientImpl.getInstance()).start();

        URL url = getClass().getResource("fxml/OpeningMenu.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setTitle("BattleGrids");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        ClientImpl.getInstance().stop();
    }
}
