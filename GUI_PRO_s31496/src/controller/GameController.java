package controller;

import model.*;
import table.BoardTableModel;
import view.GameView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private volatile Direction direction = Direction.NONE;

    private final GameBoard       board;
    private final BoardTableModel model;
    private final GameView        view;
    private final Thread          pacThread;
    private volatile boolean gameFinished = false;
    private Thread timerThread;
    private Pacman pac;
    private final GameStats stats = new GameStats();
    private final List<Enemy> enemies = new ArrayList<>();



    public GameController(BoardPreset preset) {
        this(preset.rows(), preset.cols(), preset);
    }

    private GameController(int rows, int cols, BoardPreset preset) {

        board = new GameBoard(rows, cols);
        MapGenerator.generate(board, preset);

        model = new BoardTableModel(board);

        view  = new GameView(model, cols, stats);

        timerThread = new Thread(() -> {
            try {
                while (!gameFinished && stats.getSecond() > 0) {
                    Thread.sleep(1000);
                    if (!stats.isTimerFrozen())
                        stats.decSecond();
                    SwingUtilities.invokeLater(view.getHud()::refresh);
                }
                if (!gameFinished)
                    SwingUtilities.invokeLater(() -> endGame("Time up!"));
            } catch (InterruptedException ignored) { }
        });
        timerThread.start();


        attachKeyListener();

        int spawnC = cols / 2;
        int spawnR = rows / 2;
        while (spawnR > 1 && board.getTile(spawnR, spawnC) == Tile.WALL)
            spawnR--;
        board.setCell(spawnR, spawnC, "R0");
        model.fireTableCellUpdated(spawnR, spawnC);

        pac = new Pacman(this, board, model, spawnR, spawnC);
        setPacmanPosition(spawnR, spawnC);

        pacThread = new Thread(pac);
        pacThread.start();

        Enemy e1 = new Enemy( board, model, 1, 1, Enemy.Colour.RED , stats);
        Enemy e2 = new Enemy(board, model, rows - 2, cols - 2, Enemy.Colour.PINK ,stats);
        Enemy e3 = new Enemy(board, model, rows - 3, cols - 3, Enemy.Colour.YELLOW, stats);

        enemies.add(e1);
        enemies.add(e2);
        enemies.add(e3);

        new Thread(e1).start();
        new Thread(e2).start();
        new Thread(e3).start();


        addQuitShortcut();
    }

    private void attachKeyListener() {
        JFrame f = view.getFrame();
        f.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP    -> direction = Direction.UP;
                    case KeyEvent.VK_DOWN  -> direction = Direction.DOWN;
                    case KeyEvent.VK_LEFT  -> direction = Direction.LEFT;
                    case KeyEvent.VK_RIGHT -> direction = Direction.RIGHT;
                }
            }
        });
        f.setFocusable(true);
        f.requestFocusInWindow();
    }

    private void addQuitShortcut() {
        KeyStroke ks = KeyStroke.getKeyStroke("ctrl shift Q");
        JRootPane root = view.getFrame().getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks,"QUIT");
        root.getActionMap().put("QUIT", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                stop();
                view.getFrame().dispose();
                new MenuController();
            }
        });
    }

    public Direction getDirection() { return direction; }

    public void onPacmanAt(int r, int c, Object cellObj) {

        /* upgrade */
        if (cellObj instanceof model.Upgrade up) {
            switch (up.getType()) {
                case HEART      -> stats.addLife(1);
                case CHERRY -> {
                    if (!stats.isEnemyFrozen()) {
                        stats.freezeEnemy(true);
                        new Thread(() -> {
                            try { Thread.sleep(5_000); } catch (InterruptedException ignored){}
                            stats.freezeEnemy(false);
                        }).start();
                    }
                }
                case STRAWBERRY -> {
                    if (!stats.isTimerFrozen()) {
                        stats.freezeTimer(true);
                        new Thread(() -> {
                            try { Thread.sleep(10_000);} catch(InterruptedException ignored){}
                            stats.freezeTimer(false);
                        }).start();
                    }
                }
                case APPLE      -> stats.addScore(100);
                case KEY -> stats.setKey(true);
            }
            board.setCell(r, c, null);
            model.fireTableCellUpdated(r, c);
        }

        /* pellet */
        if (board.getTile(r, c) == Tile.PELLET) {
            board.setTile(r, c, Tile.EMPTY);
            stats.addScore(10);
            model.fireTableCellUpdated(r, c);
        }

        if (cellObj instanceof String s && s.startsWith("G")) {
            if (stats.hasKey()) {
                board.setCell(r, c, null);
                stats.setKey(false);
                stats.addScore(1000);

                for (Enemy enemy : enemies) {
                    if (enemy.getRow() == r && enemy.getCol() == c) {
                        enemy.stop();
                        enemies.remove(enemy);
                        break;
                    }
                }
            } else {
                stats.addLife(-1);
                if (stats.getLives() < 0) {
                    endGame("You lost all hearts!");
                    return;
                }
                respawnPacman();
            }
            model.fireTableCellUpdated(r, c);
        }

        SwingUtilities.invokeLater(view.getHud()::refresh);
    }



    private void respawnPacman() {
        direction = Direction.NONE;

        int c = board.getCols() / 2;
        int r = board.getRows() / 2;
        while (r > 1 && board.getTile(r, c) == Tile.WALL) r--;

        for (int i=0;i<board.getRows();i++)
            for (int j=0;j<board.getCols();j++)
                if (board.getCell(i,j) instanceof String s && s.length()==2)
                    board.setCell(i,j,null);

        board.setCell(r, c, "R0");
        model.fireTableCellUpdated(r, c);

        setPacmanPosition(r,c);
    }

    private void endGame(String msg) {
        if (gameFinished) return;
        gameFinished = true;

        timerThread.interrupt();
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(
                    view.getFrame(),
                    msg + "\nEnter nickname for high-score:");

            if (name != null && !name.isBlank())
                HighScoreManager.add(name.trim(), stats.getScore());

            view.getFrame().dispose();
            new MenuController();
        });

        stop();
    }
    public void setPacmanPosition(int r, int c) {
        pac.setPosition(r, c);
    }



    public void stop() { pacThread.interrupt(); }
}
