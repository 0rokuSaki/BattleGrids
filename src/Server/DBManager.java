package Server;

import Shared.GameScoreData;

import java.util.ArrayList;

/**
 * The DBManager interface defines methods for interacting with a database to manage user data and game scores.
 * It provides functionality to add users, set and retrieve password hashes, check for user existence,
 * retrieve game score data, and update game score records.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public interface DBManager {

    /**
     * Adds a new user to the database with the given username and password hash.
     *
     * @param username The username of the new user.
     * @param passwordHash The password hash associated with the user.
     * @return A message indicating the success or failure of the operation.
     */
    String addUser(String username, String passwordHash);

    /**
     * Sets the password hash for a given username.
     *
     * @param username The username for which to set the password hash.
     * @param passwordHash The new password hash to set.
     * @return A message indicating the success or failure of the operation.
     */
    String setPasswordHash(String username, String passwordHash);

    /**
     * Retrieves the password hash associated with a given username.
     *
     * @param username The username for which to retrieve the password hash.
     * @return The password hash associated with the username.
     */
    String getPasswordHash(String username);

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check for existence.
     * @return `true` if the user exists, `false` otherwise.
     */
    Boolean userExists(String username);

    /**
     * Retrieves game score data for a specific game.
     *
     * @param gameName The name of the game to retrieve score data for.
     * @return An ArrayList of GameScoreData objects representing the game scores.
     */
    ArrayList<GameScoreData> getGameScoreData(String gameName);

    /**
     * Updates game score data for a specific user and game.
     *
     * @param username The username of the player whose score is being updated.
     * @param gameName The name of the game for which the score is being updated.
     * @param result The result of the game (e.g., "win," "lose," "tie").
     * @return A message indicating the success or failure of the operation.
     */
    String updateGameScoreData(String username, String gameName, String result);
}