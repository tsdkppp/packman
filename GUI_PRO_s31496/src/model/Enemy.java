package model;


import controller.Direction;
import table.BoardTableModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.util.*;

public class Enemy implements Runnable {

    private static final int MOVE_DELAY     = 250;
    private static final int ANIM_DELAY     = 120;
    private static final long UPGRADE_CHECK = 10_000;
    private static final double UPGRADE_CHANCE = 0.25;

    private final GameBoard board;
    private final BoardTableModel model;
    private final Colour colour;
    private int row, col, frame=0;
    private Direction dir;
    private Object under = null;
    private volatile boolean running = true;
    private final GameStats stats;

    public static final Map<Colour, ImageIcon[]> FRAMES = new EnumMap<>(Colour.class);

    private final Random rng = new Random();

    static {
        load(Colour.RED,   "redghost");
        load(Colour.PINK,  "pink");
        load(Colour.YELLOW,"yellow");
    }

    public Enemy(GameBoard b, BoardTableModel m, int startR, int startC, Colour colour, GameStats stats){
        this.board=b; this.model=m; this.row=startR; this.col=startC; this.colour=colour;
        this.stats = stats;
        this.dir = Direction.values()[rng.nextInt(4)];
        under = board.getCell(row, col);
        if (isSprite(under)) under = null;
        board.setCell(row, col, tag());
    }

    private static void load(Colour c, String base){
        ImageIcon[] arr = new ImageIcon[3];
        try{
            for(int i=0;i<3;i++){
                arr[i]=new ImageIcon(
                        ImageIO.read(new File("./pacman-art/ghosts/"+base+(i+1)+".png")));
            }
        }catch(Exception ex){ex.printStackTrace();}
        FRAMES.put(c,arr);
    }


    @Override public void run() {
        long lastMove=0, lastAnim=0, lastUpgrade=0;

        while(running){
            if (stats.isEnemyFrozen()) {
                try { Thread.sleep(7); } catch (InterruptedException e){return;}
                continue;
            }
            long now = System.currentTimeMillis();

            if(now-lastAnim >= ANIM_DELAY){
                frame=(frame+1)%3;
                board.setCell(row,col, tag());
                model.fireTableCellUpdated(row,col);
                lastAnim=now;
            }

            if (now - lastMove >= MOVE_DELAY) {

                int oldR = row, oldC = col;

                int[] nxt = nextPos(dir);
                if (isPassable(nxt[0], nxt[1])) {
                    moveTo(nxt[0], nxt[1]);
                } else {
                    dir = randomPassableDirection();
                }
                lastMove = now;

                if (now - lastUpgrade >= UPGRADE_CHECK  && rng.nextDouble() < UPGRADE_CHANCE) {
                    board.setCell(oldR, oldC, Upgrade.random());
                    model.fireTableCellUpdated(oldR, oldC);
                    lastUpgrade = now;
                }
            }

            try{ Thread.sleep(15); } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    private String tag(){ return "G"+frame+"_"+colour.name(); }

    private int[] nextPos(Direction d){
        return switch(d){
            case UP    -> new int[]{row-1,col};
            case DOWN  -> new int[]{row+1,col};
            case LEFT  -> new int[]{row,col-1};
            case RIGHT -> new int[]{row,col+1};
            default    -> new int[]{row,col};
        };
    }
    private boolean isPassable(int r,int c){
        return r>=0 && r<board.getRows() && c>=0 && c<board.getCols()
                && board.getTile(r,c)!=Tile.WALL;
    }
    private void moveTo(int r, int c) {

        if (!isSprite(under)) {
            board.setCell(row, col, under);
            model.fireTableCellUpdated(row, col);
        }

        Object nextUnder = board.getCell(r, c);
        under = isSprite(nextUnder) ? null : nextUnder;

        row = r;  col = c;
        board.setCell(row, col, tag());
        model.fireTableCellUpdated(row, col);
    }



    private Direction randomPassableDirection(){
        List<Direction> options=new ArrayList<>();
        for(Direction d:EnumSet.of(Direction.UP,Direction.DOWN,Direction.LEFT,Direction.RIGHT)){
            int[] n=nextPos(d);
            if(isPassable(n[0],n[1])) options.add(d);
        }
        return options.isEmpty()?Direction.NONE: options.get(rng.nextInt(options.size()));
    }
    private static boolean isSprite(Object o) {
        return o instanceof String s &&
                (s.startsWith("G") || s.length() == 2);
    }

    public void stop() {
        running = false;
    }
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public enum Colour { RED, PINK, YELLOW }
}
