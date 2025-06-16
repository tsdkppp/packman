package view;

import controller.Direction;
import model.Enemy;
import model.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

public class GameCellRenderer extends DefaultTableCellRenderer {

    private static final Color WALL_COLOR   = new Color(0, 0, 180);
    private static final Color FLOOR_COLOR  = Color.black;

    private static final Map<Direction, ImageIcon[]> FRAMES = new EnumMap<>(Direction.class);
    static {
        load(Direction.RIGHT, "pacman-right");
        load(Direction.LEFT,  "pacman-left");
        load(Direction.UP,    "pacman-up");
        load(Direction.DOWN,  "pacman-down");
    }

    private static void load(Direction dir, String folder) {
        ImageIcon[] arr = new ImageIcon[3];
        try {
            for (int i = 0; i < 3; i++) {
                arr[i] = new ImageIcon(ImageIO.read(
                        new File("./pacman-art/" + folder + "/" + (i + 1) + ".png")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        FRAMES.put(dir, arr);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object val, boolean isSel, boolean hasFocus,
            int row, int col) {

        JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                table, val, isSel, hasFocus, row, col);
        lbl.setOpaque(true);
        lbl.setText(null);

        if (val instanceof String tag && tag.length() == 2) {
            char dirCh = tag.charAt(0);
            int  idx   = Character.getNumericValue(tag.charAt(1));

            Direction dir = switch (dirCh) {
                case 'L' -> Direction.LEFT;
                case 'R' -> Direction.RIGHT;
                case 'U' -> Direction.UP;
                case 'D' -> Direction.DOWN;
                default  -> Direction.RIGHT;
            };
            lbl.setBackground(FLOOR_COLOR);
            lbl.setIcon(FRAMES.get(dir)[idx]);
            return lbl;
        }
        else if (val instanceof String s && s.startsWith("G")){
            int idx = s.charAt(1)-'0';          // 0-2
            Enemy.Colour collor = Enemy.Colour.valueOf(
                    s.substring(3));

            lbl.setBackground(Color.black);
            lbl.setIcon( Enemy.FRAMES.get(collor)[idx] );
            return lbl;
        }

        if (val instanceof model.Upgrade up) {
            lbl.setBackground(FLOOR_COLOR);
            lbl.setHorizontalAlignment(CENTER);
            lbl.setVerticalAlignment(CENTER);
            lbl.setIcon(up.getIcon());
            return lbl;
        }

        if (val instanceof Tile tile) {
            switch (tile) {
                case WALL -> {
                    lbl.setBackground(WALL_COLOR);
                    lbl.setIcon(null);
                }
                case PELLET -> {
                    lbl.setBackground(FLOOR_COLOR);
                    lbl.setIcon(new Icon() {
                        private final int d = 6;
                        public int getIconWidth()  { return d; }
                        public int getIconHeight() { return d; }
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            g.setColor(new Color(255,200,0));
                            g.fillOval(x, y, d, d);
                        }
                    });
                }

                default -> {
                    lbl.setBackground(FLOOR_COLOR);
                    lbl.setIcon(null);
                }
            }
            return lbl;
        }

        lbl.setBackground(FLOOR_COLOR);
        lbl.setIcon(null);
        return lbl;
    }
}
