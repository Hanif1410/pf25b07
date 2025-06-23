import java.awt.*;

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = 420;
    public static final int CANVAS_HEIGHT = 620;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_SIZE = Cell.SIZE;

    public static final int X_PADDING = (CANVAS_WIDTH - (CELL_SIZE * COLS)) / 2;
    public static final int Y_PADDING = (CANVAS_HEIGHT - (CELL_SIZE * ROWS)) / 2;

    public static final Color COLOR_GRID = Color.WHITE;
    public static final int Y_OFFSET = 6;

    Cell[][] cells;

    public Board() {
        initGame();
    }

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].clear();
            }
        }
    }

    /** RESET seluruh isi papan jadi kosong (NO_SEED) */
    public void clear() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].content = Seed.NO_SEED;
            }
        }
    }

    /**
     * Cek apakah move ke (row, col) valid:
     * - masih di dalam area papan
     * - cell-nya kosong
     */
    public boolean validMove(int row, int col) {
        return (row >= 0 && row < ROWS && col >= 0 && col < COLS && cells[row][col].content == Seed.NO_SEED);
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        if (cells[selectedRow][0].content == player
                && cells[selectedRow][1].content == player
                && cells[selectedRow][2].content == player
                || cells[0][selectedCol].content == player
                && cells[1][selectedCol].content == player
                && cells[2][selectedCol].content == player
                || selectedRow == selectedCol
                && cells[0][0].content == player
                && cells[1][1].content == player
                && cells[2][2].content == player
                || selectedRow + selectedCol == 2
                && cells[0][2].content == player
                && cells[1][1].content == player
                && cells[2][0].content == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            return State.DRAW;
        }
    }

    public void paint(Graphics g) {
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(
                    X_PADDING,
                    Y_PADDING + CELL_SIZE * row - GRID_WIDTH_HALF,
                    CELL_SIZE * COLS,
                    GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH
            );
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(
                    X_PADDING + CELL_SIZE * col - GRID_WIDTH_HALF,
                    Y_PADDING,
                    GRID_WIDTH,
                    CELL_SIZE * ROWS,
                    GRID_WIDTH, GRID_WIDTH
            );
        }
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g, X_PADDING, Y_PADDING);
            }
        }
    }
}
