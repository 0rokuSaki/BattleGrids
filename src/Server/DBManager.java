package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {
    public void deleteUsersTable() throws SQLException;
    public void createUsersTable() throws ClassNotFoundException, SQLException;
    public String printUsersTable();
    public String addUser(String username, String password);
    public String setPassword(String username, String password);
    public String getPassword(String username);
    public Boolean isUserNameExist(String username) throws SQLException;
    public Boolean validLogIn(String username, String password) throws SQLException;

}