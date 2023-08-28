package Client.Controllers;

import Shared.GameSession;

/**
 * The GameController interface defines methods that a game controller class should implement.
 * A game controller is responsible for initializing, updating, and terminating a game session.
 * This interface provides a contract for implementing game-specific logic.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August, 2023
 */
public interface GameController {

    /**
     * Initializes the game controller with the provided game session.
     * This method is called at the beginning of the game to set up initial conditions.
     *
     * @param gameSession The game session to be initialized.
     */
    void initializeGame(GameSession gameSession);

    /**
     * Updates the game controller with the current state of the game session.
     * This method is called periodically to update the game's logic and state.
     *
     * @param gameSession The current state of the game session.
     */
    void updateGame(GameSession gameSession);

    /**
     * Terminates the game controller and ends the game session.
     * This method is called when the game is completed or needs to be stopped.
     *
     * @param message A message indicating the reason for terminating the game.
     */
    void terminateGame(String message);
}