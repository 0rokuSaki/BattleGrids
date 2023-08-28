package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The `Server` interface defines the methods that a remote server can provide to clients via RMI (Remote Method Invocation).
 * It extends the `Remote` interface, which is a marker interface for identifying remote objects.
 *
 * <p> This interface provides methods for handling various requests from clients, such as login, logout, registration,
 * password change, game list retrieval, game play, move making, quitting games, and retrieving game score data.
 * These methods are designed to be invoked remotely by clients, allowing for communication between the client and server.
 *
 * <p> This interface is designed to support distributed applications using RMI technology.
 *
 * <p> This interface is part of the shared package, intended for use by both the client and server components.
 *
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public interface Server extends Remote {

    /**
     * Handles a login request from a client.
     *
     * @param username The username provided by the client.
     * @param password The password provided by the client.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleLoginRequest(String username, String password) throws RemoteException;

    /**
     * Handles a logout request from a client.
     *
     * @param username The username of the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    void handleLogoutRequest(String username) throws RemoteException;

    /**
     * Handles a registration request from a client.
     *
     * @param username The desired username for registration.
     * @param password The password provided by the client.
     * @param passwordVerification The password verification provided by the client.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException;

    /**
     * Handles a password change request from a client.
     *
     * @param username The username of the client.
     * @param oldPassword The old password provided by the client.
     * @param newPassword The new password provided by the client.
     * @param newPasswordVerification The new password verification provided by the client.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException;

    /**
     * Handles a request to retrieve the list of available games.
     *
     * @return An ArrayList of game names.
     * @throws RemoteException If a remote communication error occurs.
     */
    ArrayList<String> handleGetGamesListRequest() throws RemoteException;

    /**
     * Handles a game play request from a client.
     *
     * @param username The username of the client.
     * @param gameName The name of the game to play.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handlePlayGameRequest(String username, String gameName) throws RemoteException;

    /**
     * Handles a request to make a move in a game session.
     *
     * @param sessionNumber The session number of the game.
     * @param row The row index of the move on the game board.
     * @param col The column index of the move on the game board.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleMakeMoveRequest(long sessionNumber, int row, int col) throws RemoteException;

    /**
     * Handles a request to quit a game session.
     *
     * @param username The username of the client.
     * @param sessionNumber The session number of the game.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleQuitGameRequest(String username, long sessionNumber) throws RemoteException;

    /**
     * Handles a request to quit a game by name.
     *
     * @param username The username of the client.
     * @param gameName The name of the game to quit.
     * @return An empty string if no error occurred, or an error message.
     * @throws RemoteException If a remote communication error occurs.
     */
    String handleQuitGameRequest(String username, String gameName) throws RemoteException;

    /**
     * Handles a request to retrieve the list of game score data for a specific game.
     *
     * @param gameName The name of the game.
     * @return An ArrayList of GameScoreData instances.
     * @throws RemoteException If a remote communication error occurs.
     */
    ArrayList<GameScoreData> handleGetScoreListRequest(String gameName) throws RemoteException;
}
