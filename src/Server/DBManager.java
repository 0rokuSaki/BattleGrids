package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {






    public void deleteUsersTable(Statement statement) throws SQLException;
    public void createUsersTable(Statement statement) throws ClassNotFoundException, SQLException;
    public void printUsersTable(Statement statement) throws SQLException;
    public void addUser(Statement statement, String username, String password) throws SQLException;
    public void setUserName(Statement statement, String username);
    public void setPassword(Statement statement, String password);
    public void getPassword(Statement statement) throws SQLException;
    public boolean isUserNameExist(Statement statement, String username) throws SQLException;
    public boolean validLogIn(Statement statement, String password);








}
