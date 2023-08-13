package Client.Controllers;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class TicTacToeGameController extends GameControllerBase {

    private static final int GRID_SIZE = 3;

    public TicTacToeGameController() {
        super();
        gameName = "Tic Tac Toe";
        instructions = "Be the first to get three of your symbols (X or O) in a row, column, or diagonal.";
    }

    @Override
    protected void initializeGrid() {
        grid = new GridPane();
        grid.setGridLinesVisible(true);

        // Set the bottom row with buttons numbered 1 to GRID_SIZE
        buttons = new Button[GRID_SIZE * GRID_SIZE];
        double buttonWidth = grid.widthProperty().divide(GRID_SIZE).doubleValue();
        double buttonHeight = grid.heightProperty().divide(GRID_SIZE).doubleValue();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i ++) {
            buttons[i] = new Button();
            buttons[i].setPrefSize(gridRootPane.getPrefWidth() / GRID_SIZE, gridRootPane.getPrefHeight() / GRID_SIZE);
            buttons[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttons[i].setMinSize(0, 0);
            buttons[i].setPrefSize(buttonWidth, buttonHeight);
            buttons[i].setOnAction(this::handleGameButtonPress);
            int row = i / GRID_SIZE;    // Calculate the row number
            int col = i % GRID_SIZE;    // Calculate the column number
            grid.add(buttons[i], col, row);
        }
        if (!username.equals(gameSession.getCurrTurn())) {
            buttonsSetDisable(true);
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
    protected void updateGrid() {

    }

    @Override
    protected void handleGameButtonPress(ActionEvent event) {

    }


}
