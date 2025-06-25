import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyAIPlayer extends AIPlayer {
    private Random rand = new Random();

    public EasyAIPlayer(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    availableMoves.add(new int[]{row, col});
                }
            }
        }
        return availableMoves.get(rand.nextInt(availableMoves.size()));
    }
}
