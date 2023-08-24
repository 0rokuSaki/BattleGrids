package Shared;

import java.io.Serializable;

public class GameScoreData implements Serializable {
    private final String username;
    private final int won;
    private final int lost;
    private final int tie;

    public GameScoreData(String username, int won, int lost, int tie) {
        this.username = username;
        this.won = won;
        this.lost = lost;
        this.tie = tie;
    }

    public String getUsername() {
        return username;
    }

    public int getWon() {
        return won;
    }

    public int getLost() {
        return lost;
    }

    public int getTie() {
        return tie;
    }
}
