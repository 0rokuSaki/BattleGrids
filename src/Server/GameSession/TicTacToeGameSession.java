package Server.GameSession;

public class TicTacToeGameSession extends GameSessionBase{

    private static final int GRID_SIZE = 3;

    public TicTacToeGameSession(String player1, String player2) {
        super("Tic Tac Toe", player1, player2);

        // Initialize a 2D String array representing the game board
        gameBoard = new String[GRID_SIZE][GRID_SIZE];

        movesLeft = GRID_SIZE * GRID_SIZE;
    }

    @Override
    public String[][] getGameBoard() {
        return gameBoard;
    }

    @Override
    public boolean legalMove(int row, int col) {
        return gameBoard[row][col] == null;
    }

    @Override
    public void makeMove(int row, int col) {
        if (getWinner() != null) { // Do not make a move if there is a winner
            return;
        }
        gameBoard[row][col] = getCurrTurn();
        checkForWinner(row, col);
        if (getWinner() == null) {
            currTurn = (currTurn.equals(player1) ? player2 : player1);
        }
        movesLeft--;          // One less move available
        tie = movesLeft == 0; // It's a tie if no one won and no moves available
    }

    private void checkForWinner(int row, int col) {
        boolean isWinningTurn = false;

        // Check row
        if (currTurn.equals(gameBoard[row][0]) && currTurn.equals(gameBoard[row][1]) && currTurn.equals(gameBoard[row][2])) {
            isWinningTurn = true;
        }

        // Check column
        else if (currTurn.equals(gameBoard[0][col]) && currTurn.equals(gameBoard[1][col]) && currTurn.equals(gameBoard[2][col])) {
            isWinningTurn = true;
        }

        // Check diagonals
        else if (row == col && currTurn.equals(gameBoard[0][0]) && currTurn.equals(gameBoard[1][1]) && currTurn.equals(gameBoard[2][2])) {
            isWinningTurn = true; // Main diagonal (top-left to bottom-right)
        }

        else if (row + col == 2 && currTurn.equals(gameBoard[0][2]) && currTurn.equals(gameBoard[1][1]) && currTurn.equals(gameBoard[2][0])) {
            isWinningTurn = true; // Anti-diagonal (top-right to bottom-left)
        }

        if (isWinningTurn) {
            winner = currTurn;
        }
    }
}
