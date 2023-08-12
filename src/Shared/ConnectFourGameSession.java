package Shared;

import java.io.Serializable;

public class ConnectFourGameSession implements Serializable {
    public enum Player {
        RED,
        BLUE
    }

    private final static int NUM_CONNECTED = 4;
    private final static int GRID_SIZE = 7;
    private Player[][] gameBoard;
    private Player currPlayer;
    private Player winner;

    public ConnectFourGameSession() {
        // initialize a 2d int array representing the game board
        gameBoard = new Player[GRID_SIZE-1][GRID_SIZE];
        currPlayer = Player.RED;
        winner = null;
    }

    public Player[][] getGameBoard() {
        return gameBoard;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    private boolean checkForWinner(int row, int col) {
        int count = 1, x = row, y = col;

        // Check horizontally
        while (count != NUM_CONNECTED && y + 1 < gameBoard.length && gameBoard[x][y + 1] == currPlayer) {
            count++;
            y++;
        }
        y = col;
        while (count != NUM_CONNECTED && y - 1 >= 0 && gameBoard[x][y - 1] == currPlayer) {
            count++;
            y--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        y = col;

        // Check vertically
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && gameBoard[x+1][y] == currPlayer) {
            count++;
            x++;
        }
        x = row;
        while (count != NUM_CONNECTED && x-1 >= 0 && gameBoard[x-1][y] == currPlayer) {
            count++;
            x--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        x = row;

        // Check first diagonal
        while (count != NUM_CONNECTED && x-1 >= 0 && y+1 < gameBoard[0].length && gameBoard[x-1][y+1] == currPlayer) {
            count++;
            x--; y++;
        }
        x = row; y = col;
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && y-1 >= 0 && gameBoard[x+1][y-1] == currPlayer) {
            count++;
            x++; y--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        x = row; y = col;

        // Check second diagonal
        while (count != NUM_CONNECTED && x-1 >= 0 && y-1 >= 0 && gameBoard[x-1][y-1] == currPlayer) {
            count++;
            x--; y--;
        }
        x = row; y = col;
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && y+1 < gameBoard[0].length &&
                gameBoard[x+1][y+1] == currPlayer) {
            count++;
            x++; y++;
        }

        // Set winner
        winner = currPlayer;
        return count == NUM_CONNECTED;
    }
}
