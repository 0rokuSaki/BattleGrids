package Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.*;

import static java.sql.DriverManager.*;

public class DBManagerImpl implements DBManager {

    private Statement statement;
    private Connection connection;

    public DBManagerImpl()  {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");//loading driver
            connection = getConnection("jdbc:mysql://localhost:3306", "root", "root");//making connection
            statement = connection.createStatement();//creating statement
        }catch (Exception e)
        {
            //what should I do here???!!!
        }

    }

    public void deleteUsersTable() {
        try{
            statement.executeUpdate("DROP TABLE IF EXISTS users");//deleting table "users"
        }catch (Exception exception)
        {
            System.out.println("cant delete table");
        }
    }

    public void createUsersTable() throws SQLException {
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydatabase");
        statement.executeUpdate("USE mydatabase");////using mydatabase
        deleteUsersTable();
        statement.executeUpdate("CREATE TABLE users (username varchar (10) PRIMARY KEY, password varchar (10))");//creating table "users"
    }

    public String printUsersTable()  {
        try
        {
            ResultSet resultset = statement.executeQuery("SELECT * FROM users");
            while (resultset.next()) {
                String usernameValue = resultset.getString("username");
                String passwordValue = resultset.getString("password");
                System.out.println("Username: " + usernameValue + ", Password: " + passwordValue);
            }
            return("UsersTable");
        }catch (SQLException exception)
        {
            return null;
        }

    }

    public String addUser(String username, String password) {
        try{
            statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "')");
            return (username);
        }catch (SQLException exception)
        {
            return null;
        }

    } //להוסיף בדיקה האם בשם משתמש קיים

    public String setPassword(String username, String password) {
        try {
            // Check if the user exists in the table
            String checkUserQuery = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUserQuery);
            if (!resultSet.next()) {
                resultSet.close();
                return "";//user name doesnt exist
            }

            // Update the password for the user
            String updateQuery = "UPDATE users SET password = '" + password + "' WHERE username = '" + username + "'";
            statement.executeUpdate(updateQuery);

            resultSet.close();
            return password;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getPassword(String username) {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT password FROM users WHERE username = 'u2'");
            if (resultSet.next())
            {
                String password = resultSet.getString("password");
                resultSet.close();
                return(password);
            }
            else {
                resultSet.close();
                return("");
            }
        }catch (SQLException exception)
        {
            return (null);
        }
    }

    public Boolean isUserNameExist(String u) throws SQLException {
        try
        {
            return(statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + u + "')").next() && statement.getResultSet().getBoolean(1));
        }catch (Exception exception)
        {
            return null;
        }
    }

    public Boolean validLogIn(String username, String password) throws SQLException {
        try
        {
            return (statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + username + "' and password='"+password+"')").next() && statement.getResultSet().getBoolean(1));
        }catch (SQLException exception)
        {
            return null;
        }
    }
}