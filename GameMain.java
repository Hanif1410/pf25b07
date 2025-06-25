import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
//a
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar, scoreBoard, iconHeader, timerLabel;
    private GameMode gameMode;
    private Seed botSeed;
    private AIPlayer aiPlayer;
    private int aiLevel;

    private int scoreCross = 0, scoreNought = 0, scoreDraw = 0;
    private boolean soundPlayed = false, isTimerOn = false, dialogShown = false;
    private int timeRemaining = 10;
    private Timer turnTimer;

    public static Image iconStar, iconMushroom;
    private Image bgImage;

    private String playerXName = "Player X", playerOName = "Player O";

    public GameMain() {
        initGame();

        setLayout(new BorderLayout());
        iconStar = new ImageIcon(getClass().getResource("/assets/star.png")).getImage();
        iconMushroom = new ImageIcon(getClass().getResource("/assets/mushroom.png")).getImage();
        bgImage = new ImageIcon(getClass().getResource("/assets/Tic Tac Toy!.jpg")).getImage();

        iconHeader = new JLabel(new ImageIcon(iconStar), JLabel.CENTER);
        iconHeader.setPreferredSize(new Dimension(420, 100));
        add(iconHeader, BorderLayout.NORTH);

        scoreBoard = new JLabel();
        scoreBoard.setFont(FONT_STATUS);
        scoreBoard.setBackground(COLOR_BG_STATUS);
        scoreBoard.setOpaque(true);
        scoreBoard.setHorizontalAlignment(JLabel.CENTER);
        add(scoreBoard, BorderLayout.PAGE_START);

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);

        timerLabel = new JLabel();
        timerLabel.setFont(FONT_STATUS);
        timerLabel.setBackground(COLOR_BG_STATUS);
        timerLabel.setOpaque(true);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(statusBar);
        bottomPanel.add(timerLabel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 50));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        SoundEffect.initGame();
        initTurnTimer();
        pilihModePermainan();
        newGame();
    }

    private void pilihModePermainan() {
        String[] turnOption = {"X", "O"};
        int turnChoice = JOptionPane.showOptionDialog(null, "Siapa yang mulai duluan?", "Giliran Pertama",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, turnOption, turnOption[0]);
        currentPlayer = (turnChoice == 0) ? Seed.CROSS : Seed.NOUGHT;

        String[] option = {"VS AI", "2 Player"};
        int choice = JOptionPane.showOptionDialog(null, "Pilih Mode", "Mode Permainan",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

        if (choice == 0) {
            gameMode = GameMode.AI;
            botSeed = (turnChoice == 0) ? Seed.NOUGHT : Seed.CROSS;

            String[] levels = {"Easy", "Medium", "Hard"};
            int levelChoice = JOptionPane.showOptionDialog(null, "Pilih AI Level:", "AI Level",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
            if (levelChoice < 0) System.exit(0);
            aiLevel = levelChoice;
        } else {
            gameMode = GameMode.Player;
            playerXName = inputName("Player X (X)");
            playerOName = inputName("Player O (O)");
        }

        int timerChoice = JOptionPane.showOptionDialog(null, "Aktifkan timer giliran?", "Timer",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ya", "Tidak"}, "Ya");
        isTimerOn = (timerChoice == 0);
        timerLabel.setVisible(isTimerOn);
    }

    private String inputName(String label) {
        String name = JOptionPane.showInputDialog(null, "Masukkan nama " + label + ":", "Nama", JOptionPane.PLAIN_MESSAGE);
        return (name == null || name.trim().isEmpty()) ? label : name;
    }

    private void initGame() {
        board = new Board();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState != State.PLAYING) return;
                int row = (e.getY() - Board.Y_PADDING) / Cell.SIZE;
                int col = (e.getX() - Board.X_PADDING) / Cell.SIZE;
                if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                        && board.cells[row][col].content == Seed.NO_SEED) {
                    SoundEffect.CLICK.play();
                    currentState = board.stepGame(currentPlayer, row, col);
                    updateScoreAndSwitch();
                    repaint();
                }
            }
        });
    }

    private void initTurnTimer() {
        turnTimer = new Timer(1000, e -> {
            if (!isTimerOn) return;
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining + " s");
            if (timeRemaining <= 0 && currentState == State.PLAYING) {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                if (gameMode == GameMode.AI && currentPlayer == botSeed) {
                    int[] move = aiPlayer.move();
                    board.cells[move[0]][move[1]].content = botSeed;
                    currentState = board.stepGame(botSeed, move[0], move[1]);
                    currentPlayer = (botSeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                }
                timeRemaining = 10;
                repaint();
            }
        });
        turnTimer.start();
    }

    private void updateScoreAndSwitch() {
        if (currentState == State.CROSS_WON) scoreCross++;
        else if (currentState == State.NOUGHT_WON) scoreNought++;
        else if (currentState == State.DRAW) scoreDraw++;
        updateScoreBoard();
        timeRemaining = 10;
        if (isTimerOn) turnTimer.restart();
        if (currentState == State.PLAYING) {
            if (gameMode == GameMode.AI) {
                currentPlayer = botSeed;
                int[] move = aiPlayer.move();
                board.cells[move[0]][move[1]].content = botSeed;
                currentState = board.stepGame(botSeed, move[0], move[1]);
                if (currentState == State.CROSS_WON) scoreCross++;
                else if (currentState == State.NOUGHT_WON) scoreNought++;
                else if (currentState == State.DRAW) scoreDraw++;
                updateScoreBoard();
                currentPlayer = (botSeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                timeRemaining = 10;
            } else {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            }
        }
    }

    public void newGame() {
        board.newGame();
        currentState = State.PLAYING;
        soundPlayed = false;
        dialogShown = false;
        timeRemaining = 10;
        if (isTimerOn) turnTimer.restart();

        if (gameMode == GameMode.AI) {
            switch (aiLevel) {
                case 0 -> aiPlayer = new EasyAIPlayer(board);
                case 1 -> aiPlayer = new MediumAIPlayer(board);
                case 2 -> aiPlayer = new HardAIPlayer(board);
            }
            aiPlayer.setSeed(botSeed);
        }
    }

    private void updateScoreBoard() {
        scoreBoard.setText("X: " + scoreCross + "  |  O: " + scoreNought + "  |  Draw: " + scoreDraw);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        setOpaque(false);
        board.paint(g);

        String statusText;
        if (currentState == State.PLAYING) {
            iconHeader.setIcon(new ImageIcon(currentPlayer == Seed.CROSS ? iconStar : iconMushroom));
            statusText = (gameMode == GameMode.Player)
                    ? ((currentPlayer == Seed.CROSS ? playerXName : playerOName) + "'s Turn")
                    : (currentPlayer == Seed.CROSS ? "X's Turn" : "O's Turn");
            statusBar.setForeground(Color.BLACK);
        } else {
            if (!soundPlayed) {
                if (currentState == State.DRAW) SoundEffect.BG_MUSIC.play();
                else SoundEffect.GAME_OVER.play();
                soundPlayed = true;
            }
            statusText = (currentState == State.DRAW) ? "Draw!" :
                    (currentState == State.CROSS_WON ? "X Wins!" : "O Wins!");
            statusBar.setForeground(Color.RED);

            if (!dialogShown) {
                dialogShown = true;
                SwingUtilities.invokeLater(() -> {
                    int opt = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                            "Game Over!\nMain lagi?", "Restart", JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        newGame();
                        repaint();
                    } else {
                        System.exit(0);
                    }
                });
            }
        }
        statusBar.setText(statusText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }
}
