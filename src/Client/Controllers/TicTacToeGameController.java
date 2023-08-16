package Client.Controllers;

import Client.ClientModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TicTacToeGameController extends GameControllerBase {

    private static final int GRID_SIZE = 3;

    private Button[][] buttons;

    public TicTacToeGameController() {
        super();
        gameName = "Tic Tac Toe";
        instructions = "Be the first to get three of your symbols (X or O) in a row, column, or diagonal.";
    }

    @Override
    protected void initializeGrid() {
        grid = new GridPane();
        grid.setGridLinesVisible(true);

        buttons = new Button[GRID_SIZE][GRID_SIZE];

        // Row and column constraints for an equal 3x3 grid
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / GRID_SIZE);
        rowConstraints.setValignment(VPos.CENTER);

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(100.0 / GRID_SIZE);
        colConstraints.setHalignment(HPos.CENTER);

        for (int row = 0; row < GRID_SIZE; row++) {
            grid.getRowConstraints().add(rowConstraints);
            grid.getColumnConstraints().add(colConstraints);

            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = new Button();
                button.setId(("" + row) + col);
                button.setTextAlignment(TextAlignment.CENTER);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setPrefSize(gridRootPane.getPrefWidth() / GRID_SIZE, gridRootPane.getPrefHeight() / GRID_SIZE);
                button.setOnAction(this::handleGameButtonPress);
                button.widthProperty().addListener((observable, oldValue, newValue) -> {
                    buttonSizeChangeHandler(button);
                });
                button.heightProperty().addListener((observable, oldValue, newValue) -> {
                    buttonSizeChangeHandler(button);
                });
                buttons[row][col] = button;
                grid.add(buttons[row][col], col, row);
            }
        }
        if (!username.equals(gameSession.getCurrTurn())) {
            buttonsSetDisable(true);
        }

        // Make GridPane's width to be 75% of parent, and center it.
        // Bind GridPane size to AnchorPane size for responsiveness.
        grid.translateXProperty().bind(gridRootPane.widthProperty().subtract(grid.widthProperty()).divide(2));
        grid.prefWidthProperty().bind(gridRootPane.widthProperty().multiply(0.75));
        grid.prefHeightProperty().bind(gridRootPane.heightProperty());

        gridRootPane.getChildren().add(grid);
    }

    @Override
    protected void updateGrid() {
        String[][] gameBoard = gameSession.getGameBoard();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (gameBoard[row][col] == null) continue;
                String shape = gameBoard[row][col].equals(gameSession.getPlayer1()) ? "X" : "O";  // Player 1 is X
                Color color = username.equals(gameBoard[row][col]) ? Color.BLUE : Color.RED;      // user is blue
                buttons[row][col].setText(shape);
                buttons[row][col].setTextFill(color);
            }
        }
    }

    @Override
    protected void handleGameButtonPress(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = button.getId().charAt(0) - '0';
        int col = button.getId().charAt(1) - '0';

        if (!gameSession.legalMove(row, col)) return;

        String shape = username.equals(gameSession.getPlayer1()) ? "X" : "O";
        button.setText(shape);
        button.setTextFill(Color.BLUE);
        buttonsSetDisable(true);
        String returnMsg = ClientModel.getInstance().makeMove(gameSession.getSessionNumber(), row, col);
        if (!returnMsg.equals("")) {
            new Alert(Alert.AlertType.ERROR, returnMsg, ButtonType.OK);
        }
    }

    @Override
    protected void buttonsSetDisable(boolean val) {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(val);
            }
        }
    }

    private void buttonSizeChangeHandler(Button button) {
        Platform.runLater(() ->{
            double minDim = Math.min(gridRootPane.getScene().getWidth(), gridRootPane.getScene().getHeight());
            button.setFont(Font.font(0.075 * minDim)); // Make font size 7.5% of scene's minimum width and height.
        });
    }
}
