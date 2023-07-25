package Client;

import Shared.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Hash = " + CredentialsManager.generatePasswordHash(""));


        // Set up connection to server
        final String HOST_ADDR = "localhost";
        final int HOST_PORT = 54321;

        // Locate server's registry
        Registry registry = LocateRegistry.getRegistry(HOST_ADDR, HOST_PORT);

        // Obtain a reference to GameServer
        Server serverStub = (Server) registry.lookup("GameServer");

        // Set server stub
        ServerStubHolder.getInstance().setServerStub(serverStub);

        URL url = getClass().getResource("fxml/OpeningMenu.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setTitle("BattleGrids");
        stage.setScene(scene);
        stage.show();
    }
}
