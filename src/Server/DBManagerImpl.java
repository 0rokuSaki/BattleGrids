package Server;
import Shared.GameScoreData;

import java.rmi.RemoteException;
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
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS games (username varchar (255), gameName char (32), win int, draw int, lose int, PRIMARY KEY (username, gameName))");

    }


    public String deleteUsersTable()
    {
        try
        {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            statement.executeUpdate("CREATE TABLE users (" + "username varchar(255) PRIMARY KEY, " + "passwordHash char(32), " + "connectFourWins int, " + "ticTacToeWins int)");
            return("users table deleted");
        }catch (SQLException exception)
        {
            return null;
        }

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

    public String printGamesTable()  {
        try
        {
            ResultSet resultset = statement.executeQuery("SELECT * FROM games");
            while (resultset.next()) {
                String usernameValue = resultset.getString("username");
                String gameNameValue = resultset.getString("gameName");
                String winValue = resultset.getString("win");
                String drawValue = resultset.getString("draw");
                String loseValue = resultset.getString("lose");
                System.out.println("Username: " + usernameValue + ", game: " + gameNameValue+ ", win: " + winValue+", draw: "+drawValue+", lose: "+loseValue);
            }
            return("GamesTable");
        }catch (SQLException exception)
        {
            return null;
        }

    }

    public String addUser(String username, String passwordHash) {
        try{
            statement.executeUpdate("INSERT INTO users (username, passwordHash) VALUES ('" + username + "', '" + passwordHash + "')");
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
            String query = "SELECT username, win, draw, lose FROM games WHERE gameName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gameName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                result.add(new GameScoreData(resultSet.getString("username"), resultSet.getInt("win"), resultSet.getInt("draw"), resultSet.getInt("lose")));

            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException exception) {
            return null;
        }
    }

    public String updateGameScoreData(String username, String gameName, String result) {
        try {
            String updateQuery = "INSERT INTO games (username, gameName, win, draw, lose) " +
                    "VALUES (?, ?, CASE WHEN ? = 'win' THEN 1 ELSE 0 END, " +
                    "CASE WHEN ? = 'draw' THEN 1 ELSE 0 END, " +
                    "CASE WHEN ? = 'lose' THEN 1 ELSE 0 END) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "win = win + CASE WHEN ? = 'win' THEN 1 ELSE 0 END, " +
                    "draw = draw + CASE WHEN ? = 'draw' THEN 1 ELSE 0 END, " +
                    "lose = lose + CASE WHEN ? = 'lose' THEN 1 ELSE 0 END";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, gameName);
            preparedStatement.setString(3, result);
            preparedStatement.setString(4, result);
            preparedStatement.setString(5, result);
            preparedStatement.setString(6, result);
            preparedStatement.setString(7, result);
            preparedStatement.setString(8, result);

            preparedStatement.executeUpdate(); // Execute the query

            preparedStatement.close();
            return result;

        } catch (SQLException exception) {
            System.err.println("SQL Exception: " + exception.getMessage());
            return null;
        }
    }
/*
    public String updateGameScoreData(String username, String gameName, String result) {

        try {
            String checkQuery = "SELECT username FROM games WHERE username = '" + username + "' AND gameName = '" + gameName + "'";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            if (!resultSet.next()) {
                String insertQuery = "INSERT INTO games (username, gameName, win, draw, lose) " +
                        "VALUES ('" + username + "', '" + gameName + "', 0, 0, 0)";
                statement.executeUpdate(insertQuery);
            }



            resultSet.close();



            return result;

        } catch (SQLException exception) {
            System.err.println("SQL Exception: " + exception.getMessage());
            return null;
        }



    }











    @Override
    public String updateGameScoreData(String username, String gameName, String result) {
        try {
            String updateQuery = "INSERT INTO games (username, gameName, win, draw, lose) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "win = win + ?, " +
                    "draw = draw + ?, " +
                    "lose = lose + ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, gameName);
            int win = 0, draw=0, lose=0;
            if (result.equals("win")) {win = 1;}
            else if (result.equals("draw")) {draw = 1;}
            else if (result.equals("lose")) {lose = 1;}

            preparedStatement.setInt(3, win);
            preparedStatement.setInt(4, draw);
            preparedStatement.setInt(5, lose);

            preparedStatement.setInt(6, win);
            preparedStatement.setInt(7, draw);
            preparedStatement.setInt(8, lose);

            preparedStatement.executeUpdate(); // Execute the query

            preparedStatement.close();
            return result;
        } catch (SQLException exception) {
            System.err.println("SQL Exception: " + exception.getMessage());

            return null;
        }
    }





    @Override
    public String updateGameScoreData(String username, String gameName, String result) {

        try {
            String updateQuery = "INSERT INTO games (username, gameName, win, draw, lose) " +
                    "VALUES (?, ?, CASE WHEN ? = 'win' THEN 1 ELSE 0 END, " +
                    "CASE WHEN ? = 'draw' THEN 1 ELSE 0 END, " +
                    "CASE WHEN ? = 'lose' THEN 1 ELSE 0 END) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "win = win + CASE WHEN ? = 'win' THEN 1 ELSE 0 END, " +
                    "draw = draw + CASE WHEN ? = 'draw' THEN 1 ELSE 0 END, " +
                    "lose = lose + CASE WHEN ? = 'lose' THEN 1 ELSE 0 END";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, gameName);
            preparedStatement.executeUpdate(); // Execute the query




            //preparedStatement.close();
            return result;

        } catch (SQLException exception) {
            return null;
        }
    }

 */
}