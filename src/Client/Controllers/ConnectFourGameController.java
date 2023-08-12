package Client.Controllers;

import Client.ClientModel;
import Shared.GameSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ConnectFourGameController implements GameController {

    private final int GRID_SIZE = 7;

    @FXML
    private HBox buttonsContainer;

    @FXML
    private Label infoLabel;

    @FXML
    private VBox labelsContainer;

    @FXML
    private Pane gridRootPane;

    private GridPane grid;

    private Button[] buttons;

    private Long sessionNumber;

    @FXML
    void initialize() {
        ClientModel.getInstance().setGameController(this);
        // Set info label
        infoLabel.setText("Waiting for player...");
        infoLabel.setTextFill(Color.GREEN);

        Platform.runLater(() -> {
            // Set size of gridRoot in the window
            AnchorPane.setLeftAnchor(gridRootPane, 0.0);
            AnchorPane.setRightAnchor(gridRootPane, 0.0);
            AnchorPane.setTopAnchor(gridRootPane, labelsContainer.getHeight());
            AnchorPane.setBottomAnchor(gridRootPane, buttonsContainer.getHeight());
        });
    }

    @FXML
    void instructionsButtonPress(ActionEvent event) {
    }

    @FXML
    void quitGameButtonPress(ActionEvent event) {

    }

    public void initializeGame(GameSession gameSession) {
        // Set session number
        sessionNumber = gameSession.getSessionNumber();

        // Set opponent name
        String username = ClientModel.getInstance().getUsername();
        String player1 = gameSession.getPlayer1();
        String player2 = gameSession.getPlayer2();
        String opponentName = username.equals(player1) ? player2 : player1;
        infoLabel.setText("Opponent: " + opponentName);
        infoLabel.setTextFill(Color.BLACK);

        // Create a 7 by 7 grid
        grid = new GridPane();
        grid.setGridLinesVisible(true);

        // set the bottom row with buttons numbered 1 to GRID_SIZE
        double buttonWidth = grid.widthProperty().divide(GRID_SIZE).doubleValue();
        double buttonHeight = grid.heightProperty().divide(GRID_SIZE).doubleValue();

        buttons = new Button[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i ++) {
            buttons[i] = new Button("" + (i + 1));
            buttons[i].setPrefSize(gridRootPane.getPrefWidth() / GRID_SIZE, gridRootPane.getPrefHeight() / GRID_SIZE);
            buttons[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttons[i].setMinSize(0, 0);
            buttons[i].setPrefSize(buttonWidth, buttonHeight);
            grid.add(buttons[i], i, GRID_SIZE - 1); // add button to grid
        }

        if (!username.equals(gameSession.getCurrTurn())) {
            deactivateButtons();
        }

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(100.0 / GRID_SIZE); // Equal width columns
        for (int i = 0; i < GRID_SIZE; i++) {
            grid.getColumnConstraints().add(colConstraints);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / GRID_SIZE); // Equal height rows
        for (int i = 0; i < GRID_SIZE; i++) {
            grid.getRowConstraints().add(rowConstraints);
        }

        // Bind GridPane size to AnchorPane size for responsiveness
        grid.prefWidthProperty().bind(gridRootPane.widthProperty());
        grid.prefHeightProperty().bind(gridRootPane.heightProperty());

        gridRootPane.getChildren().add(grid);
    }

    private void activateButtons() {
        for (Button button : buttons) {
            button.setDisable(false);
        }
    }

    private void deactivateButtons() {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }
}