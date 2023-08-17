package Server;

import Shared.GameScoreData;

public interface DBManager {
    String printUsersTable();
    String addUser(String username, String passwordHash);
    String setPasswordHash(String username, String passwordHash);
    String getPasswordHash(String username);
    Boolean userExists(String username);

    GameScoreData getGameScoreData(String username, String gameName);
}