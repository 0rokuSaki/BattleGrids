package Shared;

import java.io.Serializable;

/**
 * The `GameScoreData` class represents the game performance data of a player.
 * It holds information about the username, number of games won, lost, and tied.
 * The class implements the `Serializable` interface, allowing instances to be serialized.
 *
 * <p> This class provides getter methods to access the username, number of games won, lost, and tied.
 *
 * <p> This class is part of the shared package, intended for use by both the client and server components.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public class GameScoreData implements Serializable {
    private final String username;
    private final int won;
    private final int lost;
    private final int tie;

    /**
     * Constructs a `GameScoreData` instance with the provided parameters.
     *
     * @param username The username associated with the game scores.
     * @param won The number of games won by the player.
     * @param lost The number of games lost by the player.
     * @param tie The number of games tied by the player.
     */
    public GameScoreData(String username, int won, int lost, int tie) {
        this.username = username;
        this.won = won;
        this.lost = lost;
        this.tie = tie;
    }

    /**
     * Retrieves the username associated with the game scores.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the number of games won by the player.
     *
     * @return The number of games won.
     */
    public int getWon() {
        return won;
    }

    /**
     * Retrieves the number of games lost by the player.
     *
     * @return The number of games lost.
     */
    public int getLost() {
        return lost;
    }

    /**
     * Retrieves the number of games tied by the player.
     *
     * @return The number of games tied.
     */
    public int getTie() {
        return tie;
    }
}
