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

    public DBManagerImpl()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");//loading driver
            connection = getConnection("jdbc:mysql://localhost:3306", "root", "root");//making connection
            statement = connection.createStatement();//creating statement
        }catch (Exception e)
        {
            //add
        }

    }

    public void deleteUsersTable() throws SQLException
    {
        statement.executeUpdate("DROP TABLE IF EXISTS users");//deleting table "users"
    }

    public void createUsersTable() throws ClassNotFoundException, SQLException
    {
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydatabase");
        statement.executeUpdate("USE mydatabase");////using mydatabase
        deleteUsersTable();
        statement.executeUpdate("CREATE TABLE users (username varchar (10) PRIMARY KEY, password varchar (10))");//creating table "users"
    }


    public String printUsersTable() throws SQLException
    {
        try
        {
            ResultSet resultset = statement.executeQuery("SELECT * FROM users");
            while (resultset.next()) {
                String usernameValue = resultset.getString("username");
                String passwordValue = resultset.getString("password");
                System.out.println("Username: " + usernameValue + ", Password: " + passwordValue);
            }
            return ("need to add something here");
        }catch (SQLException e)
        {
            return null;
        }

    }


    public String addUser(String username, String password) throws SQLException
    {
        try{
            statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "')");
            return (username);
        }catch (SQLException e)
        {
            return null;
        }

    }


/*
    public String setPassword(String password)
    {
        try{
            return (password);
        }catch ()
        {
            return null;
        }

    }

    public String getPassword() throws SQLException
    {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT password FROM users WHERE username = 'u2'");
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                return(password);
            } else {
                System.out.println("User not found");
            }
            resultSet.close();
        }catch (SQLException SQLexception)
        {
            return (null);
        }

    }

    public Boolean isUserNameExist(String u) throws SQLException {
        //ResultSet resultSet = statement.executeQuery("EXISTS * FROM users WHERE username = 'u'");
        String s = "SELECT EXISTS (SELECT 1 FROM users WHERE username = '" + u + "')";
        System.out.println(s);
        return true;
    }

    public Boolean validLogIn(String password)
    {
        System.out.println("validLogIn");
        return true;
    }
*/


}



