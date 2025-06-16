package view;

import model.GameStats;

import javax.swing.*;
import java.awt.*;

public class HudPanel extends JPanel {

    private final JLabel scoreLbl = new JLabel();
    private final JLabel timeLbl  = new JLabel();
    private final JLabel lifeLbl  = new JLabel();
    private final JLabel keyLbl   = new JLabel();

    private final GameStats stats;

    public HudPanel(GameStats s) {
        stats = s;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        add(scoreLbl);
        add(timeLbl);
        add(lifeLbl);
        add(keyLbl);


        refresh();
    }

    public void refresh() {
        scoreLbl.setText("Score: " + stats.getScore());
        timeLbl.setText("Time: " + stats.getSecond());
        lifeLbl.setText("Lives: " + stats.getLives());
       keyLbl.setText("Key: " + stats.hasKey());
        timeLbl.setForeground(stats.isTimerFrozen()
                ? Color.CYAN
                : Color.BLACK);
    }
}
