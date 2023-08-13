package Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSession implements Serializable {
    private final static List<Long> sessionNumbers = new ArrayList<>();

    private Long sessionNumber;
    private final String player1;
    private final String player2;
    private String currTurn;
    private String winner;
    private boolean playerQuit;

    public GameSession(String player1, String player2) {
        Random random = new Random(); // Assign a random session number
        do {                          // Make sure the number is unique
            sessionNumber = random.nextLong();
        } while (sessionNumbers.contains(sessionNumber));
        sessionNumbers.add(sessionNumber);

        this.player1 = player1;
        this.player2 = player2;
        currTurn = player1;
        winner = null;
        playerQuit = false;
    }

    public void releaseNumber() {
        sessionNumbers.remove(sessionNumber);
    }

    public Long getSessionNumber() {
        return sessionNumber;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getCurrTurn() {
        return currTurn;
    }

    public String getWinner() {
        return winner;
    }

    public String[][] getGameBoard() {
        return null;
    }

    public boolean getPlayerQuit() {
        return playerQuit;
    }

    public void updateCurrTurn() {
        currTurn = (currTurn.equals(player1) ? player2 : player1);
    }

    public void setWinnerAsCurrent() {
        winner = currTurn;
    }

    public void setPlayerQuit() {
        playerQuit = true;
        updateCurrTurn();
        setWinnerAsCurrent();
    }

    public boolean legalMove(int row, int col) { return false; }

    public void makeMove(int row, int col) {}
}
