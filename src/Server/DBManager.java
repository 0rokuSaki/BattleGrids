package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {
    public void deleteUsersTable() throws SQLException;
    public void createUsersTable() throws ClassNotFoundException, SQLException;
    public String printUsersTable() throws SQLException;
    public String addUser(String username, String password) throws SQLException;
    //public String setPassword(String password);
    //public String getPassword() throws SQLException;
    //public Boolean isUserNameExist(String username) throws SQLException;
    //public Boolean validLogIn(String password);

}
