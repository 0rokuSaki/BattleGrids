package Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSession implements Serializable {
    private final static List<Long> sessionNumbers = new ArrayList<>();

    private Long sessionNumber;
    private String player1;
    private String player2;
    private String currTurn;

    public GameSession(String player1, String player2) {
        // Assign a random session number
        Random random = new Random();
        do {
            sessionNumber = random.nextLong();
        } while (sessionNumbers.contains(sessionNumber));
        sessionNumbers.add(sessionNumber);

        this.player1 = player1;
        this.player2 = player2;
        currTurn = player1;
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
}
