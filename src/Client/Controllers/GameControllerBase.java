package Client.Controllers;

import Client.ClientModel;
import Shared.GameSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * The abstract GameControllerBase class provides a base implementation for game controllers.
 * It defines common methods and fields that are shared among different game controllers.
 * Subclasses should extend this class and implement the abstract methods for specific game logic.
 * This class also handles GUI setup, updating, and termination for the game.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August 2023
 */
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

    // Fields for game state
    protected GameSession gameSession;
    protected String username;
    protected String opponentName;
    protected String gameName;
    protected String instructions;

    /**
     * Constructor for GameControllerBase.
     * Initializes the GameControllerBase with necessary information from the ClientModel.
     */
    protected GameControllerBase() {
        ClientModel.getInstance().setGameController(this);
        username = ClientModel.getInstance().getUsername();
    }

    /**
     * Initializes the GUI components and sets up the initial state of the game.
     * This method is automatically called when the FXML file is loaded.
     */
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

    /**
     * Opens a dialog displaying the game's instructions.
     */
    @FXML
    protected void instructionsButtonPress() {
        Alert a = new Alert(Alert.AlertType.INFORMATION, instructions);
        a.setHeaderText("Instructions");
        a.showAndWait();
    }

    /**
     * Handles the user's request to quit the game.
     * If the game has started, prompts the user to confirm quitting.
     * If confirmed, terminates the game session and returns to the games menu.
     *
     * @param event The ActionEvent triggered by the quit button press.
     */
    @FXML
    protected void quitGameButtonPress(ActionEvent event) {
        String returnMsg;
        if (gameSession != null) { // If game already started
            if (userWantsToQuit()) {
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

    /**
     * Initializes the game controller with the provided game session.
     * This method is called at the beginning of the game to set up initial conditions.
     *
     * @param gameSession The game session to be initialized.
     */
    @Override
    public void initializeGame(GameSession gameSession) {
        this.gameSession = gameSession;
        this.opponentName = username.equals(gameSession.getPlayer1()) ? gameSession.getPlayer2() : gameSession.getPlayer1();

        // Set warning message when attempting to close the window
        Stage stage = (Stage) gridRootPane.getScene().getWindow();
        stage.setOnCloseRequest(this::handleOnCloseRequest);

        updateLabels();
        initializeGrid();
    }

    /**
     * Updates the game view with the current state of the game session.
     *
     * @param gameSession The current state of the game session.
     */
    @Override
    public void updateGame(GameSession gameSession) {
        this.gameSession = gameSession;
        boolean gameFinished = (gameSession.getWinner() != null) || gameSession.getPlayerQuit() || gameSession.getTie();
        String alertContextText = null;
        if (gameSession.getWinner() != null) {
            if (gameSession.getPlayerQuit()) {
                alertContextText = "You win! " + opponentName + " quit the game.";
            }
            else {
                alertContextText = gameSession.getWinner() + " won the game!";
            }
        }
        else if (gameSession.getTie()) {
            alertContextText = "It's a tie!";
        }
        updateGrid();
        updateLabels();
        if (gameFinished) {
            new Alert(Alert.AlertType.INFORMATION, alertContextText).showAndWait();
            changeScene(gridRootPane.getScene(), "GamesMenu.fxml");
        }
        if (!gameFinished && username.equals(gameSession.getCurrTurn())) {
            buttonsSetDisable(false); // Activate buttons if its the user's turn
        }
    }

    /**
     * Terminates the game controller and ends the game session.
     * This method is called when the game is completed or needs to be stopped.
     *
     * @param message A message indicating the reason for terminating the game.
     */
    @Override
    public void terminateGame(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
        changeScene(gridRootPane.getScene(), "GamesMenu.fxml");
    }

    /**
     * Changes the scene to the specified FXML file.
     * The previous scene and the name of the FXML file are provided as parameters.
     *
     * @param prevScene The previous scene from which the transition is initiated.
     * @param fxmlName The name of the FXML file (including path) for the new scene.
     */
    @Override
    protected void changeScene(Scene prevScene, String fxmlName) {
        ClientModel.getInstance().setGameController(null);
        Stage stage = (Stage) prevScene.getWindow();
        stage.setOnCloseRequest(event -> {});
        super.changeScene(prevScene, fxmlName);
    }

    /**
     * Updates the labels in the user interface to reflect the current game state.
     */
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

    /**
     * Handles the action of closing the game window.
     * If the user confirms, the game is terminated and the application is closed.
     *
     * @param event The Event triggered by the window close request.
     */
    protected void handleOnCloseRequest(Event event) {
        if (userWantsToQuit()) { // If the user confirms, close the application
            String returnMsg = ClientModel.getInstance().quitGame(gameSession.getSessionNumber());
            if (!returnMsg.equals("")) {
                new Alert(Alert.AlertType.ERROR, returnMsg, ButtonType.OK).showAndWait();
            }
            Platform.exit();
        }
        event.consume(); // Consume the event to prevent the window from closing
    }

    /**
     * Initializes the grid component of the game interface.
     * Subclasses should implement this method to set up the initial grid layout.
     */
    protected abstract void initializeGrid();

    /**
     * Updates the grid component of the game interface with the latest state.
     * Subclasses should implement this method to update the grid based on game logic.
     */
    protected abstract void updateGrid();

    /**
     * Handles the press of a game-related button in the user interface.
     * Subclasses should implement this method to define the behavior of the buttons.
     *
     * @param event The ActionEvent triggered by the button press.
     */
    protected abstract void handleGameButtonPress(ActionEvent event);

    /**
     * Sets the disable property of game-related buttons in the user interface.
     * Subclasses should implement this method to enable or disable buttons as needed.
     *
     * @param val The value to set for the disable property (true to disable, false to enable).
     */
    protected abstract void buttonsSetDisable(boolean val);

    /**
     * Determines whether the user intends to quit the game.
     * Displays a confirmation dialog and returns the user's decision.
     *
     * @return `true` if the user intends to quit, `false` otherwise.
     */
    private boolean userWantsToQuit() {
        String warningMessage = "Quitting will cause you to lose the match. Are you sure?";
        Optional<ButtonType> buttonPressed =
                new Alert(Alert.AlertType.WARNING, warningMessage, ButtonType.OK, ButtonType.CANCEL).showAndWait();
        return (buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK);
    }
}
