package Server;

import java.sql.SQLException;

public interface DBManager {
    public void deleteUsersTable() throws SQLException;
    public void createUsersTable() throws ClassNotFoundException, SQLException;
    public String printUsersTable();
    public int hashCode(String password);
    public String addUser(String username, String password);
    public String validPassword (String password); //נדרש? איפה מתבצעת הבדיקה?
    public String setPassword(String username, String oldPassword, String newPassword);
    public String getPassword(String username);
    public Boolean isUserNameExist(String username);
    public Boolean validLogIn(String username, String password);

}