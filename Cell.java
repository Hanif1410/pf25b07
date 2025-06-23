import java.awt.*;

public class Cell {
    public static final int SIZE = 130;  // ukuran satu kotak
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    int row, col;
    Seed content;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        clear();
    }

    public void clear() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g, int xOffset, int yOffset) {
        int x1 = xOffset + col * SIZE + PADDING;
        int y1 = yOffset + row * SIZE + PADDING;

        if (content == Seed.CROSS) {
            g.drawImage(GameMain.iconStar, x1, y1, SEED_SIZE, SEED_SIZE, null);
        } else if (content == Seed.NOUGHT) {
            g.drawImage(GameMain.iconMushroom, x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }
}