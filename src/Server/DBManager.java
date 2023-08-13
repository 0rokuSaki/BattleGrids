package Server;

import java.sql.SQLException;

public interface DBManager {
    public String printUsersTable();
    public String addUser(String username, String passwordHash);
    public String setPasswordHash(String username, String passwordHash);
    public String getPasswordHash(String username);
    public Integer setConnectFourWins(String username, int connectFourWins);
    public Integer getConnectFourWins(String username);
    public Integer setTicTacToeWins(String username, int connectFourWins);
    public Integer getTicTacToeWins(String username);
    public Boolean userExists(String username);

}