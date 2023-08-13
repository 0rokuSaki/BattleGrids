package Client.Controllers;

import Client.ClientModel;
import Shared.GameSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Optional;

public abstract class GameControllerBase extends ControllerBase implements GameController {

    @FXML
    protected HBox buttonsContainer;

    @FXML
    protected VBox labelsContainer;

    @FXML
    protected Pane gridRootPane;

    @FXML
    protected Label opponentLabel;

    @FXML
    protected Label divideLabel;

    @FXML
    protected Label infoLabel;

    protected GridPane grid;

    protected Button[] buttons;

    protected GameSession gameSession;

    protected String username;

    protected String opponentName;

    protected String gameName;

    protected String instructions;

    protected GameControllerBase() {
        ClientModel.getInstance().setGameController(this);
        username = ClientModel.getInstance().getUsername();
    }

    @FXML
    protected void initialize() {
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
    protected void instructionsButtonPress() {
        Alert a = new Alert(Alert.AlertType.INFORMATION, instructions);
        a.setHeaderText("Instructions");
        a.showAndWait();
    }

    @FXML
    protected void quitGameButtonPress(ActionEvent event) {
        String returnMsg;
        if (gameSession != null) { // If game already started
            String warningMessage = "Quitting will cause you to lose the match. Are you sure?";
            Optional<ButtonType> buttonPressed =
                    new Alert(Alert.AlertType.WARNING, warningMessage, ButtonType.OK, ButtonType.CANCEL).showAndWait();
            if (buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
                returnMsg = ClientModel.getInstance().quitGame(gameSession.getSessionNumber());
                if (!returnMsg.equals("")) {
                    new Alert(Alert.AlertType.ERROR, returnMsg, ButtonType.OK).showAndWait();
                }
                changeScene(((Node) event.getSource()).getScene(), "GamesMenu.fxml");
            }
        }
        else {  // Game hasn't started
            returnMsg = ClientModel.getInstance().quitGame(gameName);
            if (!returnMsg.equals("")) {
                new Alert(Alert.AlertType.ERROR, returnMsg, ButtonType.OK).showAndWait();
            }
            changeScene(((Node) event.getSource()).getScene(), "GamesMenu.fxml");
        }
    }

    @Override
    public void initializeGame(GameSession gameSession) {
        this.gameSession = gameSession;
        this.opponentName = username.equals(gameSession.getPlayer1()) ? gameSession.getPlayer1() : gameSession.getPlayer2();
        updateLabels();
        initializeGrid();
    }

    @Override
    public void updateGame(GameSession gameSession) {
        this.gameSession = gameSession;
        boolean gameFinished = false;
        Alert alert = null;
        if (gameSession.getWinner() != null) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            if (gameSession.getPlayerQuit()) {
                alert.setContentText("You win! " + opponentName + " quit the game.");
            }
            else {
                alert.setContentText(gameSession.getWinner() + " won the game!");
            }
            gameFinished = true;
        }
        updateGrid();
        updateLabels();
        if (gameFinished) {
            alert.showAndWait();
            changeScene(gridRootPane.getScene(), "GamesMenu.fxml");
        }
        else if (username.equals(gameSession.getCurrTurn())) {
            buttonsSetDisable(false); // Activate buttons if its the user's turn
        }
    }

    @Override
    protected void changeScene(Scene prevScene, String fxmlName) {
        ClientModel.getInstance().setGameController(null);
        super.changeScene(prevScene, fxmlName);
    }

    protected void updateLabels() {
        String currTurn = username.equals(gameSession.getCurrTurn()) ? "Your turn" : opponentName + "'s turn";
        Color infoLabelTextFill = username.equals(gameSession.getCurrTurn()) ? Color.BLUE : Color.RED;

        // Opponent label
        opponentLabel.setText("Opponent: " + opponentName);
        opponentLabel.setTextFill(Color.RED);
        opponentLabel.setVisible(true);

        // Divide label
        divideLabel.setVisible(true);

        // Info label
        infoLabel.setText(currTurn);
        infoLabel.setTextFill(infoLabelTextFill);
        infoLabel.setVisible(true);
    }

    protected void buttonsSetDisable(boolean val) {
        for (Button button : buttons) {
            button.setDisable(val);
        }
    }

    protected abstract void initializeGrid();

    protected abstract void updateGrid();

    protected abstract void handleGameButtonPress(ActionEvent event);
}
