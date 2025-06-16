package view;

import model.HighScore;
import model.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoresView extends JDialog {

    public HighScoresView(JFrame owner) {
        super(owner, "High Scores", true);

        List<HighScore> list = HighScoreManager.load();
        DefaultListModel<String> lm = new DefaultListModel<>();
        int i = 1;
        for (HighScore hs : list)
            lm.addElement(i++ + ". " + hs.name() + "   " + hs.score());

        JList<String> jl = new JList<>(lm);
        jl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

        add(new JScrollPane(jl));
        setSize(250, 250);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
