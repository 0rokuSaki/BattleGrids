package Shared;

import java.io.Serializable;

/**
 * The `GameSession` interface defines the methods to access and manipulate the state of a game session.
 * It extends the `Serializable` interface, allowing instances to be serialized and transferred over the network.
 *
 * <p> This interface provides methods to retrieve information about the game session, including session number,
 * game name, player names, current turn, winner, game board, and game status. It also includes methods to check
 * for a legal move and make a move on the game board.
 *
 * <p> This interface is designed to be implemented by classes representing various types of games.
 *
 * <p> This interface is part of the shared package, intended for use by both the client and server components.
 *
 * <p> This interface is co-authored by Aaron Barkan and Omer Bar.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public interface GameSession extends Serializable {

    /**
     * Retrieves the session number associated with the game session.
     *
     * @return The session number.
     */
    long getSessionNumber();

    /**
     * Retrieves the name of the game.
     *
     * @return The game name.
     */
    String getGameName();

    /**
     * Retrieves the username of the first player.
     *
     * @return The username of the first player.
     */
    String getPlayer1();

    /**
     * Retrieves the username of the second player.
     *
     * @return The username of the second player.
     */
    String getPlayer2();

    /**
     * Retrieves the username of the player whose turn it currently is.
     *
     * @return The username of the player with the current turn.
     */
    String getCurrTurn();

    /**
     * Retrieves the username of the player who won the game.
     *
     * @return The username of the winner, or `null` if there is no winner yet.
     */
    String getWinner();

    /**
     * Retrieves the game board as a two-dimensional array.
     *
     * @return The game board, represented as a two-dimensional array.
     */
    String[][] getGameBoard();

    /**
     * Checks if a player has quit the game.
     *
     * @return `true` if a player has quit, `false` otherwise.
     */
    boolean getPlayerQuit();

    /**
     * Checks if the game is a tie.
     *
     * @return `true` if the game is a tie, `false` otherwise.
     */
    boolean getTie();

    /**
     * Checks if a move is legal for the current game state.
     *
     * @param row The row index of the move on the game board.
     * @param col The column index of the move on the game board.
     * @return `true` if the move is legal, `false` otherwise.
     */
    boolean legalMove(int row, int col);

    /**
     * Makes a move on the game board.
     *
     * @param row The row index of the move on the game board.
     * @param col The column index of the move on the game board.
     */
    void makeMove(int row, int col);
}
