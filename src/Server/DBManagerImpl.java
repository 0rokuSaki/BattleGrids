package Server;
import Shared.GameScoreData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import static java.sql.DriverManager.*;

public class DBManagerImpl implements DBManager {

    private Statement statement;
    private Connection connection;

    public DBManagerImpl() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");//loading driver
        connection = getConnection("jdbc:mysql://localhost:3306", "root", "root");//making connection
        statement = connection.createStatement();//creating statement
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydatabase");
        statement.executeUpdate("USE mydatabase");////using mydatabase
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar (255) PRIMARY KEY, passwordHash char (32))");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS games (username varchar (255), gameName char (32), wins int, draws int, loses int, PRIMARY KEY (username, gameName))");

    }


    public String printUsersTable()  {
        try
        {
            ResultSet resultset = statement.executeQuery("SELECT * FROM users");
            while (resultset.next()) {
                String usernameValue = resultset.getString("username");
                String passwordValue = resultset.getString("passwordHash");
                System.out.println("Username: " + usernameValue + ", passwordHash: " + passwordValue);
            }
            return("UsersTable");
        }catch (SQLException exception)
        {
            return null;
        }

    }

    public String addUser(String username, String passwordHash) {
        try{
            statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + passwordHash + "')");
            return (username+", "+passwordHash);
        }catch (SQLException exception)
        {
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
        try{
            ResultSet resultSet = statement.executeQuery("SELECT passwordHash FROM users WHERE username = '"+username+"'");
            resultSet.next();
            String passwordHash = resultSet.getString("passwordHash");
            resultSet.close();
            return(passwordHash);
        }catch (SQLException exception)
        {
            return (null);
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
            String query = "SELECT username, wins, draws, loses FROM games WHERE gameName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gameName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                result.add(new GameScoreData(resultSet.getString("username"), resultSet.getInt("wins"), resultSet.getInt("draws"), resultSet.getInt("loses")));

            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    public String updateGameScoreData(String username, String gameName, String result) {
        try {
            String updateQuery = "UPDATE games SET " + result + " = " + result + " + 1 WHERE username = ? AND gameName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, gameName);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result;

        } catch (SQLException exception) {
            return null;
        }
    }
}