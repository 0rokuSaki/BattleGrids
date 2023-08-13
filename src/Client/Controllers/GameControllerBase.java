package Client.Controllers;

import Client.ClientModel;
import Shared.GameSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Optional;

public class GameControllerBase extends ControllerBase implements GameController {

    //////////////////////////////////////////////////////
    ///////// PACKAGE-PRIVATE INSTANCE VARIABLES /////////
    //////////////////////////////////////////////////////
    @FXML
    HBox buttonsContainer;

    @FXML
    VBox labelsContainer;

    @FXML
    Pane gridRootPane;

    @FXML
    Label opponentLabel;

    @FXML
    Label divideLabel;

    @FXML
    Label infoLabel;

    GridPane grid;

    Button[] buttons;

    GameSession gameSession;

    String instructions;

    String username;

    String gameName;

    boolean gameStarted;

    //////////////////////////////////////////////////////
    //////////////////// PUBLIC METHODS //////////////////
    //////////////////////////////////////////////////////
    @Override
    public void initializeGame(GameSession gameSession) {
        this.gameSession = gameSession;
        this.gameStarted = true;
        updateLabels();
        grid = new GridPane();
        grid.setGridLinesVisible(true);
    }

    @Override
    public void updateGame(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    //////////////////////////////////////////////////////
    /////////////// PACKAGE-PRIVATE METHODS //////////////
    //////////////////////////////////////////////////////
    @FXML
    void initialize() {
        this.username = ClientModel.getInstance().getUsername();
        this.gameStarted = false;
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
        Alert a = new Alert(Alert.AlertType.INFORMATION, instructions);
        a.setHeaderText("Instructions");
        a.showAndWait();
    }

    @FXML
    void quitGameButtonPress(ActionEvent event) {
        String returnMsg;
        if (gameStarted) {
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

    void updateLabels() {
        String player1 = gameSession.getPlayer1();
        String player2 = gameSession.getPlayer2();
        String opponentName = username.equals(player1) ? player2 : player1;
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

    void activateButtons() {
        for (Button button : buttons) {
            button.setDisable(false);
        }
    }

    void deactivateButtons() {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }
}
