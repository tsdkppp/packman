package model;



import controller.BoardPreset;

public final class MapGenerator {
    private MapGenerator(){}

    public static void generate(GameBoard board, BoardPreset preset){

        String[] ascii = preset.ascii();
        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            char[] line = ascii[r].toCharArray();
            for (int c = 0; c < cols; c++) {
                switch (line[c]) {
                    case '#' -> board.setTile(r, c, Tile.WALL);
                    case '.' -> board.setTile(r, c, Tile.PELLET);
                    default  -> board.setTile(r, c, Tile.EMPTY);
                }
            }
        }
    }
}

