package Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {






    public void deleteUsersTable() throws SQLException;




    public void createUsersTable() throws ClassNotFoundException, SQLException;
    public void printUsersTable() throws SQLException;



    public void addUser(String username, String password) throws SQLException;



    public void setUserName(String username);
    public void setPassword(String password);
    public String getPassword() throws SQLException;
    public boolean isUserNameExist(String username) throws SQLException;
    public boolean validLogIn(String password);








}
