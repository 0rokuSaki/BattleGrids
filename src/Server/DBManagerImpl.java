package Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Objects;

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

    } //לחשוב איך אפשר להחזיר את הטבלה

    public int hashCode(String password)
    {
        return (Objects.hash(password));
    }

    public String addUser(String username, String password) {
        try{
            if (isUserNameExist(username))
                return("");
            //String hash= String.valueOf(hashCode(password));
            statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "')");
            return (username);
        }catch (SQLException exception)
        {
            return null;
        }

    }

    public String validPassword (String password) {
        return (password);
    } //האם הבדיקה תעשה פה?

    public String setPassword(String username, String oldPassword, String newPassword) {
        try {
            if (!validLogIn(username, oldPassword))
                return("");
            String updateQuery = "UPDATE users SET password = '" + newPassword + "' WHERE username = '" + username + "'";
            statement.executeUpdate(updateQuery);
            return newPassword;

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

    public Boolean isUserNameExist(String u) {
        try
        {
            return(statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + u + "')").next() && statement.getResultSet().getBoolean(1));
        }catch (SQLException exception)
        {
            return null;
        }
    }

    public Boolean validLogIn(String username, String password){
        try
        {
            return (statement.executeQuery("SELECT EXISTS (SELECT * FROM users WHERE username = '" + username + "' and password='"+password+"')").next() && statement.getResultSet().getBoolean(1));
        }catch (SQLException exception)
        {
            return null;
        }
    }
}