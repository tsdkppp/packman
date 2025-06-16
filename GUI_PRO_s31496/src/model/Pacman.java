package model;

import controller.Direction;
import controller.GameController;
import table.BoardTableModel;

import javax.swing.*;


public class Pacman implements Runnable  {

    private final GameController ctrl;
    private final GameBoard      board;
    private final BoardTableModel model;
    private int moveCnt=0, animCnt=0;

    private int row, col;
    private int frame = 0;

    private static final int TICK        = 20;
    private static final int MOVE_EVERY  = 4;
    private static final int ANIM_EVERY  = 3;



    public Pacman(GameController c,
                          GameBoard b, BoardTableModel m,
                          int startR, int startC) {
        this.ctrl  = c;
        this.board = b;
        this.model = m;
        this.row   = startR;
        this.col   = startC;
    }

    @Override public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                if (++animCnt>=ANIM_EVERY){
                    animCnt=0;
                    frame=(frame+1)%3;
                    board.setCell(row,col, tag(ctrl.getDirection(),frame));
                    int rr=row,cc=col;
                    SwingUtilities.invokeLater(
                            ()->model.fireTableCellUpdated(rr,cc));
                }

                if (++moveCnt >= MOVE_EVERY) {
                    moveCnt = 0;
                    int nr=row, nc=col;
                    switch (ctrl.getDirection()) {
                        case UP -> nr--; case DOWN -> nr++; case LEFT -> nc--; case RIGHT -> nc++;
                    }
                    if (nr>=0 && nr<board.getRows() && nc>=0 && nc<board.getCols()
                            && board.getTile(nr,nc)!=Tile.WALL) {

                        Object destObj = board.getCell(nr, nc);

                        board.setCell(row, col, null);
                        int oldR=row, oldC=col; row=nr; col=nc;
                        board.setCell(row, col, tag(ctrl.getDirection(),0));

                        SwingUtilities.invokeLater(() -> {
                            model.fireTableCellUpdated(oldR, oldC);
                            model.fireTableCellUpdated(row , col);
                        });
                        frame = 0;

                        ctrl.onPacmanAt(row, col, destObj);
                    }
                }

                Thread.sleep(TICK);
            }
        }catch(InterruptedException e){Thread.currentThread().interrupt();}
    }

    private static String tag(Direction d,int f){
        return switch(d){
            case LEFT->"L"+f; case RIGHT->"R"+f; case UP->"U"+f;
            case DOWN->"D"+f; default->"R"+f;
        };
    }
    public synchronized void setPosition(int r,int c){
        row = r; col = c; frame = 0; moveCnt = animCnt = 0;
    }

}
