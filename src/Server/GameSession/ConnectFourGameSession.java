package Server.GameSession;

public class ConnectFourGameSession extends GameSessionBase {

    private static final int GRID_SIZE = 7;

    private static final int NUM_CONNECTED = 4;

    private final int[] rowState;

    public ConnectFourGameSession(String player1, String player2) {
        super("Connect Four", player1, player2);

        // For each column in the grid, rowState stores the row index of the top shape
        rowState = new int[GRID_SIZE + 1];
        for (int i = 0; i < GRID_SIZE; i++) {
            rowState[i] = GRID_SIZE - 2;
        }
        // Last cell keeps the sum of all cells, and is used to determine when the grid is full
        rowState[GRID_SIZE] = GRID_SIZE * (GRID_SIZE - 1);

        // Initialize a 2D String array representing the game board
        gameBoard = new String[GRID_SIZE - 1][GRID_SIZE];

        movesLeft = GRID_SIZE * (GRID_SIZE - 1);
    }

    @Override
    public String[][] getGameBoard() {
        return gameBoard;
    }

    @Override
    public void makeMove(int row, int col) {
        if (getWinner() != null) { // Do not make a move if there is a winner
            return;
        }
        row = rowState[col];
        gameBoard[row][col] = getCurrTurn();
        checkForWinner(row, col);
        if (getWinner() == null) {
            currTurn = (currTurn.equals(player1) ? player2 : player1);
            rowState[col]--;
        }
        movesLeft--;          // One less move available
        tie = movesLeft == 0; // It's a tie if no one won and no moves available
    }

    @Override
    public boolean legalMove(int row, int col) {
        return rowState[col] != -1;
    }

    private void checkForWinner(int row, int col) {
        int count = 1, x = row, y = col;

        // check horizontally
        while (count != NUM_CONNECTED && y + 1 < gameBoard[0].length && getCurrTurn().equals(gameBoard[x][y + 1])) {
            count++;
            y++;
        }
        y = col;
        while (count != NUM_CONNECTED && y - 1 >= 0 && getCurrTurn().equals(gameBoard[x][y - 1])) {
            count++;
            y--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        y = col;

        // check vertically
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && getCurrTurn().equals(gameBoard[x+1][y])) {
            count++;
            x++;
        }
        x = row;
        while (count != NUM_CONNECTED && x-1 >= 0 && getCurrTurn().equals(gameBoard[x-1][y])) {
            count++;
            x--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        x = row;

        // check first diagonal
        while (count != NUM_CONNECTED && x-1 >= 0 && y+1 < gameBoard[0].length && getCurrTurn().equals(gameBoard[x-1][y+1])) {
            count++;
            x--; y++;
        }
        x = row; y = col;
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && y-1 >= 0 && getCurrTurn().equals(gameBoard[x+1][y-1])) {
            count++;
            x++; y--;
        }
        count = (count == NUM_CONNECTED) ? count : 1;
        x = row; y = col;

        // check second diagonal
        while (count != NUM_CONNECTED && x-1 >= 0 && y-1 >= 0 && getCurrTurn().equals(gameBoard[x-1][y-1])) {
            count++;
            x--; y--;
        }
        x = row; y = col;
        while (count != NUM_CONNECTED && x+1 < gameBoard.length && y+1 < gameBoard[0].length && getCurrTurn().equals(gameBoard[x+1][y+1])) {
            count++;
            x++; y++;
        }
        if (count == NUM_CONNECTED) {
            winner = currTurn;
        }
    }
}
