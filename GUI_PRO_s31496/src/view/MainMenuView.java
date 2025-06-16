
package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    private final JButton newGameButton;
    private final JButton highScoresButton;
    private final JButton exitButton;

    public MainMenuView() {
        super("Pacman Main Menu");
        newGameButton = new JButton("New Game");
        highScoresButton = new JButton("High Scores");
        exitButton = new JButton("Exit");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.add(newGameButton);
        panel.add(highScoresButton);
        panel.add(exitButton);

        add(panel, BorderLayout.CENTER);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public JButton getNewGameButton() { return newGameButton; }
    public JButton getHighScoresButton() { return highScoresButton; }
    public JButton getExitButton() { return exitButton; }
}
