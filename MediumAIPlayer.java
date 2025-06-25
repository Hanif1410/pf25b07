public class MediumAIPlayer extends AIPlayer {
    private int[][] preferredMoves = {
            {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
            {0, 1}, {1, 0}, {1, 2}, {2, 1}
    };

    public MediumAIPlayer(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        // 1. Menang jika bisa
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = mySeed;
                    if (isWinning(mySeed)) {
                        cells[row][col].content = Seed.NO_SEED;
                        return new int[]{row, col};
                    }
                    cells[row][col].content = Seed.NO_SEED;
                }
            }
        }


        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = oppSeed;
                    if (isWinning(oppSeed)) {
                        cells[row][col].content = Seed.NO_SEED;
                        return new int[]{row, col};
                    }
                    cells[row][col].content = Seed.NO_SEED;
                }
            }
        }


        for (int[] move : preferredMoves) {
            if (cells[move[0]][move[1]].content == Seed.NO_SEED) {
                return move;
            }
        }

        return null;
    }

    private boolean isWinning(Seed seed) {
        for (int i = 0; i < ROWS; i++) {
            if (cells[i][0].content == seed && cells[i][1].content == seed && cells[i][2].content == seed)
                return true;
            if (cells[0][i].content == seed && cells[1][i].content == seed && cells[2][i].content == seed)
                return true;
        }
        return (cells[0][0].content == seed && cells[1][1].content == seed && cells[2][2].content == seed) ||
                (cells[0][2].content == seed && cells[1][1].content == seed && cells[2][0].content == seed);
    }
}
