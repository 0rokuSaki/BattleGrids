package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuBase {
    protected void changeScene(ActionEvent event, String fxmlName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double prevWidth = stage.getWidth();
        double prevHeight = stage.getHeight();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(prevWidth);
        stage.setHeight(prevHeight);

        stage.show();
    }
}
