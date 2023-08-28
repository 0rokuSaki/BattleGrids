package Server;
import Shared.GameScoreData;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.*;

/**
 * The `DBManagerImpl` class implements the `DBManager` interface to interact with a MySQL database.
 * It provides methods for managing user data and game scores in the database.
 *
 * <p> This class establishes a connection to the database, creates necessary tables if they don't exist,
 * and implements methods to add users, update password hashes, retrieve password hashes, check for user existence,
 * retrieve game score data, and update game scores.
 *
 * <p> It utilizes JDBC (Java Database Connectivity) to interact with the MySQL database.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public class DBManagerImpl implements DBManager {

    private final Statement statement;
    private final Connection connection;

    /**
     * Constructs a `DBManagerImpl` instance by establishing a connection to the database,
     * creating necessary tables if they don't exist, and initializing the database connection.
     *
     * @param username The username to access the database.
     * @param password The password associated with the username.
     * @throws ClassNotFoundException If the MySQL JDBC driver class is not found.
     * @throws SQLException If a SQL-related error occurs during database setup.
     */
    public DBManagerImpl(String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = getConnection("jdbc:mysql://localhost:3306", username, password);
        statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydatabase");
        statement.executeUpdate("USE mydatabase");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar (255) PRIMARY KEY, passwordHash char (32))");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS games (username varchar (255), gameName char (32), win int, lose int, tie int, PRIMARY KEY (username, gameName))");
    }

    ////////////////////////////////////////////////////////
    //// The rest of the methods are implemented here...////
    ////////////////////////////////////////////////////////
    public String addUser(String username, String passwordHash) {
        try {
            statement.executeUpdate("INSERT INTO users (username, passwordHash) VALUES ('" + username + "', '" + passwordHash + "')");
            return (username+", "+passwordHash);
        } catch (SQLException exception) {
            return null;
        }
    }

    public String setPasswordHash(String username, String passwordHash) {
        try {
            String updateQuery = "UPDATE users SET passwordHash = '" + passwordHash + "' WHERE username = '" + username + "'";
            statement.executeUpdate(updateQuery);
            return passwordHash;
        } catch (SQLException exception) {
            return null;
        }
    }

    public String getPasswordHash(String username) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT passwordHash FROM users WHERE username = '"+username+"'");
            resultSet.next();
            String passwordHash = resultSet.getString("passwordHash");
            resultSet.close();
            return(passwordHash);
        } catch (SQLException exception) {
            return null;
        }
    }

    public Boolean userExists(String username) {
        try {
            return(statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + username + "')").next() && statement.getResultSet().getBoolean(1));
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    public ArrayList<GameScoreData> getGameScoreData(String gameName) {
        ArrayList<GameScoreData> result = new ArrayList<>();
        try {
            String query = "SELECT username, win, lose, tie FROM games WHERE gameName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gameName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                result.add(new GameScoreData(resultSet.getString("username"), resultSet.getInt("win"), resultSet.getInt("lose"), resultSet.getInt("tie")));

            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException exception) {
            return null;
        }
    }

    public String updateGameScoreData(String username, String gameName, String result) {
        int winNum = result.equals("win") ? 1 : 0;   // Initial win number if row does not exist
        int loseNum = result.equals("lose") ? 1 : 0; // Initial lose number if row does not exist
        int tieNum = result.equals("tie") ? 1 : 0;   // Initial tie number if row does not exist

        String winStr = result.equals("win") ? "win + 1" : "win";     // Update string for win number
        String loseStr = result.equals("lose") ? "lose + 1" : "lose"; // Update string for lose number
        String tieStr = result.equals("tie") ? "tie + 1" : "tie";     // Update string for tie number

        // This query increments the win/lose/tie number, or creates a new row if PK doesn't exist.
        String updateQuery = String.format(
                "INSERT INTO games (username, gameName, win, lose, tie) " +
                "VALUES (?, ?, %d, %d, %d) " +
                "ON DUPLICATE KEY UPDATE " +
                "win = %s, lose = %s, tie = %s", winNum, loseNum, tieNum, winStr, loseStr, tieStr
        );

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, gameName);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return result;

        } catch (SQLException exception) {
            System.err.println("SQL Exception: " + exception.getMessage());
            return null;
        }
    }
}