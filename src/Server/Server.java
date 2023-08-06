package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static java.sql.DriverManager.getConnection;

public class Server {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {


        DBManager DB = new DBManagerImpl();
        DB.createUsersTable();
        DB.addUser( "u1","p1");
        DB.addUser("u2","p2");
        DB.printUsersTable();
        //DB.setPassword("u2","p3");
        DB.printUsersTable();
        System.out.println(DB.getPassword("u1"));

        System.out.println(DB.isUserNameExist("u3"));

        System.out.println(DB.validLogIn("u2","p3"));

        System.out.println(DB.hashCode("yosi"));
    }
}