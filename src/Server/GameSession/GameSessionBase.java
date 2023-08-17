package Server.GameSession;

import Shared.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class GameSessionBase implements GameSession {
    private final static List<Long> sessionNumbers = new ArrayList<>();

    protected long sessionNumber;
    protected final String player1;
    protected final String player2;
    protected String currTurn;
    protected String winner;
    protected boolean playerQuit;
    protected boolean tie;
    protected String[][] gameBoard;
    protected int movesLeft;

    public GameSessionBase(String player1, String player2) {
        Random random = new Random(); // Assign a random session number
        do {                          // Make sure the number is unique
            sessionNumber = Math.abs(random.nextLong());
        } while (sessionNumbers.contains(sessionNumber));
        sessionNumbers.add(sessionNumber);

        this.player1 = player1;
        this.player2 = player2;
        currTurn = player1;
        winner = null;
        playerQuit = false;
        tie = false;
    }

    public void releaseNumber() {
        sessionNumbers.remove(sessionNumber);
    }

    public long getSessionNumber() {
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

    public boolean getPlayerQuit() {
        return playerQuit;
    }

    public boolean getTie() {
        return tie;
    }

    public void setPlayerQuit(String quittingPlayerName) {
        playerQuit = true;
        winner = quittingPlayerName.equals(player1) ? player2 : player1;
    }

    public abstract String[][] getGameBoard();

    public abstract boolean legalMove(int row, int col);

    public abstract void makeMove(int row, int col);
}
