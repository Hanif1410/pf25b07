public class HardAIPlayer extends AIPlayer {

    public HardAIPlayer(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = mySeed;
                    int score = minimax(false);
                    cells[row][col].content = Seed.NO_SEED;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(boolean isMaximizing) {
        State result = evaluateBoard();
        if (result == State.CROSS_WON || result == State.NOUGHT_WON || result == State.DRAW) {
            return score(result);
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = isMaximizing ? mySeed : oppSeed;
                    int score = minimax(!isMaximizing);
                    cells[row][col].content = Seed.NO_SEED;

                    bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }

        return bestScore;
    }

    private int score(State result) {
        if (result == State.DRAW) return 0;
        return (result == State.CROSS_WON && mySeed == Seed.CROSS) ||
                (result == State.NOUGHT_WON && mySeed == Seed.NOUGHT) ? 10 : -10;
    }

    private State evaluateBoard() {
        Board tempBoard = new Board();
        tempBoard.cells = cells;
        return tempBoard.stepGame(mySeed, 0, 0); // baris dan kolom dummy karena hanya evaluasi
    }
}

