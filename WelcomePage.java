import java.awt.*;
import javax.swing.*;

public class WelcomePage extends JFrame {

    public WelcomePage() {
        setTitle("Welcome to Tic Tac Toe!");
        setSize(420, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        SoundEffect.initGame();
        SoundEffect.BG_MUSIC.play();

        setLayout(null);

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/assets/welcome_bg.gif"));
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 420, 650);
        add(background);


        JButton playButton = new JButton("Play Now");
        playButton.setFont(new Font("8bit", Font.BOLD, 18));
        playButton.setBounds(110, 490, 200, 50);
        playButton.setBackground(new Color(59, 154, 255));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        background.add(playButton);

        // Tombol "Cara Main"
        JButton howToPlayButton = new JButton("Cara Main");
        howToPlayButton.setFont(new Font("8bit", Font.BOLD, 14));
        howToPlayButton.setBounds(125, 550, 170, 40);
        howToPlayButton.setBackground(new Color(255, 153, 0));
        howToPlayButton.setForeground(Color.WHITE);
        howToPlayButton.setFocusPainted(false);
        howToPlayButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        howToPlayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        background.add(howToPlayButton);

        // Event "Play Now"
        playButton.addActionListener(e -> {
            newGameMain();
            dispose();
        });

        // Event "Cara Main"
        howToPlayButton.addActionListener(e -> {
            showHowToPlay();
        });
    }

    private void showHowToPlay() {
        String rules = """
        🎮 Cara Main Tic Tac Toe:

        1. Permainan dimainkan oleh 2 pemain atau melawan AI.
        2. Pemain bergiliran menempatkan simbol X atau O pada papan 3x3.
        3. Pemain pertama yang berhasil menempatkan 3 simbolnya
           secara horizontal, vertikal, atau diagonal akan menang.
        4. Jika papan penuh dan tidak ada pemenang, maka hasil seri.
        5. Klik "Play Now" untuk mulai bermain!
        """;

        JOptionPane.showMessageDialog(this, rules, "Cara Main", JOptionPane.INFORMATION_MESSAGE);
    }

    private void newGameMain() {
        JFrame frame = new JFrame(GameMain.TITLE);
        frame.setContentPane(new GameMain());
        frame.setSize(420, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomePage wp = new WelcomePage();
            wp.setVisible(true);
        });
    }
}
