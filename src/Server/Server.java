package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static java.sql.DriverManager.getConnection;

public class Server {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");//loading driver
        Connection connection = getConnection("jdbc:mysql://localhost:3306", "root", "root");//making connection
        Statement statement = connection.createStatement();//creating statement
        DBManager DB = new DBManagerImpl();
        DB.createUsersTable(statement);

        DB.addUser(statement, "u1","p1");
        DB.addUser(statement, "u2","p2");

        DB.printUsersTable(statement);

        DB.getPassword(statement);

        DB.isUserNameExist(statement, "u2");
    }
}
