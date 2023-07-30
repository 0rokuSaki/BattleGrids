package Server;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.*;

import static java.sql.DriverManager.*;

public class DBManagerImpl implements DBManager {

    public DBManagerImpl()
    {

    }


    public void deleteUsersTable(Statement statement) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS users");//deleting table "users"
    }

    public void createUsersTable(Statement statement) throws ClassNotFoundException, SQLException
    {
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mydatabase");
        statement.executeUpdate("USE mydatabase");////using mydatabase
        deleteUsersTable(statement);
        statement.executeUpdate("CREATE TABLE users (username varchar (10) PRIMARY KEY, password varchar (10))");//creating table "users"
    }

    public void printUsersTable(Statement statement) throws SQLException
    {
        ResultSet resultset = statement.executeQuery("SELECT * FROM users");
        while (resultset.next()) {
            String usernameValue = resultset.getString("username");
            String passwordValue = resultset.getString("password");
            System.out.println("Username: " + usernameValue + ", Password: " + passwordValue);
        }
    }
    public void addUser(Statement statement,String username, String password) throws SQLException {
        statement.executeUpdate("INSERT INTO users VALUES ('" + username + "', '" + password + "')");
    }

    public void setUserName(Statement statement, String username)
    {
        System.out.println("setUserName");
    }
    public void setPassword(Statement statement, String password)
    {
        System.out.println("setPassword");
    }


    public void getPassword(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT password FROM users WHERE username = 'u2'");
        if (resultSet.next()) {
            String password = resultSet.getString("password");
            System.out.println("Password for user1: " + password);
        } else {
            System.out.println("User not found");
        }
        resultSet.close();
    }

    public boolean isUserNameExist(Statement statement, String u) throws SQLException {
        //ResultSet resultSet = statement.executeQuery("EXISTS * FROM users WHERE username = 'u'");
        String s = "SELECT EXISTS (SELECT 1 FROM users WHERE username = '" + u + "')";
        System.out.println(s);
        return true;




    }
    public boolean validLogIn(Statement statement, String password)
    {
        System.out.println("validLogIn");
        return true;
    }



}



