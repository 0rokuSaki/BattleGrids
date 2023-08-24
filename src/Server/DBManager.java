package Server;

import Shared.GameScoreData;

import java.util.ArrayList;

public interface DBManager {
    String addUser(String username, String passwordHash);
    String setPasswordHash(String username, String passwordHash);
    String getPasswordHash(String username);
    Boolean userExists(String username);
    ArrayList<GameScoreData> getGameScoreData(String gameName);
    String updateGameScoreData(String username, String gameName, String result);
}