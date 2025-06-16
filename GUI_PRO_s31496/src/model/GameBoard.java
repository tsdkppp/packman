package model;

public class GameBoard {

    private final int rows, cols;
    private final Tile[][]   tiles;
    private final Object[][] cells;

    public GameBoard(int r, int c) {
        rows = r; cols = c;
        tiles  = new Tile[r][c];
        cells  = new Object[r][c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                tiles[i][j] = Tile.EMPTY;
    }

    public Tile getTile(int r, int c)         { return tiles[r][c]; }
    public void setTile(int r, int c, Tile t) { tiles[r][c] = t; }

    public Object getCell(int r, int c)         { return cells[r][c]; }
    public void   setCell(int r, int c, Object v){ cells[r][c] = v;  }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

}
