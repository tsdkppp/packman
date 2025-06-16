package table;

import model.GameBoard;

import javax.swing.table.AbstractTableModel;

public class BoardTableModel extends AbstractTableModel {
    private final GameBoard board;
    public BoardTableModel(GameBoard b){ board=b; }

    @Override public int getRowCount(){ return board.getRows(); }
    @Override public int getColumnCount(){ return board.getCols(); }

    @Override
    public Object getValueAt(int r,int c){
        Object v=board.getCell(r,c);
        return v!=null ? v : board.getTile(r,c);
    }
    @Override
    public void setValueAt(Object v,int r,int c){
        board.setCell(r,c,v); fireTableCellUpdated(r,c);
    }
}
