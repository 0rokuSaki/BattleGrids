package Shared;

import java.io.Serializable;

public interface GameSession extends Serializable {
    long getSessionNumber();
    String getGameName();
    String getPlayer1();
    String getPlayer2();
    String getCurrTurn();
    String getWinner();
    String[][] getGameBoard();
    boolean getPlayerQuit();
    boolean getTie();
    boolean legalMove(int row, int col);
    void makeMove(int row, int col);
}
