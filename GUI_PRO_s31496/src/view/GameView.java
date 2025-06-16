package view;

import table.BoardTableModel;

import javax.swing.*;
import java.awt.*;

public class GameView {

    public static final int CELL = 20;

    private final JFrame frame = new JFrame("Pac-Man Game");
    private final JTable table;

    private final HudPanel hud;

    public GameView(BoardTableModel model, int cols, model.GameStats stats) {

        table = new JTable(model);
        table.setRowHeight(CELL);
        table.setEnabled(false);
        table.setShowGrid(false);
        table.setTableHeader(null);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultRenderer(Object.class, new GameCellRenderer());

        hud = new HudPanel(stats);

        JLayeredPane root = new JLayeredPane();
        JScrollPane scroll = new JScrollPane(table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setBounds(0, 25, cols * CELL, model.getRowCount() * CELL);

        hud.setBounds(0, 0, cols * CELL, 25);

        root.setPreferredSize(new Dimension(cols * CELL, model.getRowCount() * CELL + 25));
        root.add(scroll, Integer.valueOf(0));
        root.add(hud,   Integer.valueOf(1));

        frame.setContentPane(root);
        frame.pack();
        frame.setResizable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public JFrame getFrame()   { return frame; }
    public HudPanel getHud()   { return hud;  }
}
