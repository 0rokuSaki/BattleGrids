package Client.Controllers;

import Client.ClientModel;
import Shared.GameSession;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectFourGameController extends GameControllerBase {

    //////////////////////////////////////////////////////
    ////////////////// STATIC VARIABLES //////////////////
    //////////////////////////////////////////////////////
    private static final int GRID_SIZE = 7;

    //////////////////////////////////////////////////////
    /////////////// PACKAGE-PRIVATE METHODS //////////////
    //////////////////////////////////////////////////////
    @FXML
    void initialize() {
        super.initialize();
        this.gameName = "Connect Four";
        this.instructions = "Be the first player to connect 4 of the same colored discs in a row (either vertically, horizontally, or diagonally).\n" +
                "To place a disc, click on one of the seven buttons. The disc will be placed at the lowest free spot in the chosen column.";
    }

    //////////////////////////////////////////////////////
    //////////////////// PUBLIC METHODS //////////////////
    //////////////////////////////////////////////////////
    @Override
    public void initializeGame(GameSession gameSession) {
        super.initializeGame(gameSession);

        // Set the bottom row with buttons numbered 1 to GRID_SIZE
        buttons = new Button[GRID_SIZE];
        double buttonWidth = grid.widthProperty().divide(GRID_SIZE).doubleValue();
        double buttonHeight = grid.heightProperty().divide(GRID_SIZE).doubleValue();
        for (int i = 0; i < GRID_SIZE; i ++) {
            buttons[i] = new Button("" + (i + 1));
            buttons[i].setPrefSize(gridRootPane.getPrefWidth() / GRID_SIZE, gridRootPane.getPrefHeight() / GRID_SIZE);
            buttons[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttons[i].setMinSize(0, 0);
            buttons[i].setPrefSize(buttonWidth, buttonHeight);
            buttons[i].setOnAction(this::handleGameButtonPress);
            grid.add(buttons[i], i, GRID_SIZE - 1);
        }
        if (!username.equals(gameSession.getCurrTurn())) {
            deactivateButtons();
        }

        // Make the grid responsive by adding row and column constraints
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(100.0 / GRID_SIZE); // Equal width columns
        colConstraints.setHalignment(HPos.CENTER);
        for (int i = 0; i < GRID_SIZE; i++) {
            grid.getColumnConstraints().add(colConstraints);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / GRID_SIZE); // Equal height rows
        rowConstraints.setValignment(VPos.CENTER);
        for (int i = 0; i < GRID_SIZE; i++) {
            grid.getRowConstraints().add(rowConstraints);
        }

        // Bind GridPane size to AnchorPane size for responsiveness
        grid.prefWidthProperty().bind(gridRootPane.widthProperty());
        grid.prefHeightProperty().bind(gridRootPane.heightProperty());

        gridRootPane.getChildren().add(grid);
    }

    @Override
    public void updateGame(GameSession gameSession) {
        super.updateGame(gameSession);

        // Update grid
        String[][] gameBoard = gameSession.getGameBoard();
        final int divideCoef = GRID_SIZE * 3;
        for (int row = 0; row < GRID_SIZE - 1; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (gameBoard[row][col] == null) continue;
                Circle c = new Circle();
                c.radiusProperty().bind(
                        Bindings.min(gridRootPane.widthProperty().divide(divideCoef), gridRootPane.heightProperty().divide(divideCoef))
                );
                c.setFill((username.equals(gameBoard[row][col])) ? Color.BLUE : Color.RED);
                grid.add(c, col, row);
            }
        }

        if (gameSession.getWinner() != null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, gameSession.getWinner() + " has won the game!");
            a.showAndWait();
            changeScene(gridRootPane.getScene(), "GamesMenu.fxml");
        }
        if (username.equals(gameSession.getCurrTurn())) {
            activateButtons();  // Activate buttons if its the user's turn
        }

        updateLabels();
    }

    //////////////////////////////////////////////////////
    /////////////////// PRIVATE METHODS //////////////////
    //////////////////////////////////////////////////////
    private void handleGameButtonPress(ActionEvent event) {
        int col = Integer.parseInt(((Button) event.getSource()).getText()) - 1;
        if (!gameSession.legalMove(0, col)) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Column " + (col + 1) + " is full, choose another one");
            a.showAndWait();
            return;
        }
        deactivateButtons();
        ClientModel.getInstance().makeMove(gameSession.getSessionNumber(), 0, col); // TODO: Handle return value
    }
}