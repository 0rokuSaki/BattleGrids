package Client.Controllers;

import Client.ClientModel;
import Shared.GameScoreData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;


public class HighScoresMenuController extends ControllerBase {

    @FXML
    private ComboBox<String> gamesComboBox;

    @FXML
    private Button backButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void initialize() {
        // Get games list
        ArrayList<String> gamesList = ClientModel.getInstance().getGamesList();
        if (gamesList != null) {
            gamesComboBox.setItems(FXCollections.observableArrayList(gamesList));
        }
    }

    @FXML
    void gamesComboBoxOnAction(ActionEvent event) {
        String selectedGame = gamesComboBox.getSelectionModel().getSelectedItem();
        // TODO: Get high scores

        // Create a table
        TableView<GameScoreData> tableView = new TableView<>();
        ObservableList<GameScoreData> data = FXCollections.observableArrayList(
                new GameScoreData("User1", 1, 1, 1),
                new GameScoreData("User2", 2, 2, 2),
                new GameScoreData("User3", 3, 3, 3),
                new GameScoreData("User4", 4, 4, 4)
            );

        TableColumn<GameScoreData, String> usernameColumn = new TableColumn<>("User Name");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<GameScoreData, Integer> wonColumn = new TableColumn<>("Won");
        wonColumn.setCellValueFactory(new PropertyValueFactory<>("won"));

        TableColumn<GameScoreData, Integer> lostColumn = new TableColumn<>("Lost");
        lostColumn.setCellValueFactory(new PropertyValueFactory<>("lost"));

        TableColumn<GameScoreData, Integer> tieColumn = new TableColumn<>("Tie");
        tieColumn.setCellValueFactory(new PropertyValueFactory<>("tie"));

        tableView.getColumns().addAll(usernameColumn, wonColumn, lostColumn, tieColumn);
        tableView.setItems(data);

        // Set the column resize policy to constrained resize
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Sorting data by descending order based on the score
        tableView.getItems().sort(Comparator.comparingInt(GameScoreData::getWon).reversed());

        scrollPane.setContent(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Initialize scroll pane
        scrollPane.setVisible(true);
        VBox parent = (VBox) scrollPane.getParent();
        scrollPane.prefWidthProperty().bind(parent.widthProperty().multiply(0.6));   // 60%
        scrollPane.prefHeightProperty().bind(parent.heightProperty().multiply(0.4)); // 40%
    }

    @FXML
    void backButtonPress(ActionEvent event) {
        changeScene(((Node) event.getSource()).getScene(), "LobbyMenu.fxml");
    }
}
