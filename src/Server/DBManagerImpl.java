package Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
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
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" + "username varchar(255) PRIMARY KEY, " + "passwordHash char(32), " + "connectFourWins int, " + "ticTacToeWins int)");
        //statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username varchar (255) PRIMARY KEY, passwordHash char (32), connectFour int, ticTacToe int");//creating table "users"
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

    public Integer setConnectFourWins(String username, int connectFourWins)
    {
        try {
            String updateQuery = "UPDATE users SET connectFourWins = '" + connectFourWins + "' WHERE username = '" + username + "'";
            statement.executeUpdate(updateQuery);
            return (connectFourWins);

        } catch (SQLException exception) {
            return null;
        }
    }
    public Integer getConnectFourWins(String username)
    {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT connectFourWins FROM users WHERE username = '"+username+"'");
            resultSet.next();
            Integer connectFourWins = resultSet.getInt("connectFourWins");
            resultSet.close();
            return(connectFourWins);

        }catch (SQLException exception)
        {
            return (null);
        }
    }

    public Integer setTicTacToeWins(String username, int ticTacToeWins)
    {
        try {
            String updateQuery = "UPDATE users SET ticTacToeWins = '" + ticTacToeWins + "' WHERE username = '" + username + "'";
            statement.executeUpdate(updateQuery);
            return (ticTacToeWins);

        } catch (SQLException exception) {
            return null;
        }
    }
    public Integer getTicTacToeWins(String username)
    {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT ticTacToeWins FROM users WHERE username = '"+username+"'");
            resultSet.next();
            Integer ticTacToeWins = resultSet.getInt("ticTacToeWins");
            resultSet.close();
            return(ticTacToeWins);

        }catch (SQLException exception)
        {
            return (null);
        }
    }

    public Boolean userExists(String username) {
        try
        {
            return(statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + username + "')").next() && statement.getResultSet().getBoolean(1));
        }catch (SQLException exception)
        {
            return null;
        }
    }

}