public class MapData {

    private final char[][] originalMap;
    private char[][] map;
    private final int rows;
    private final int cols;
    private final Position playerStart;
    private final Position pinkyStart;
    private final Position inkyStart;
    private final Position blinkyStart;
    private final Position[] corners;

    public MapData(char[][] map,
                   Position playerStart,
                   Position pinkyStart,
                   Position inkyStart,
                   Position blinkyStart,
                   Position[] corners) {
        this.rows = map.length;
        this.cols = map[0].length;
        this.originalMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            this.originalMap[i] = map[i].clone();
        }
        this.map = deepCopy(originalMap);
        this.playerStart = playerStart;
        this.pinkyStart = pinkyStart;
        this.inkyStart = inkyStart;
        this.blinkyStart = blinkyStart;
        this.corners = corners;
    }

    private char[][] deepCopy(char[][] src) {
        char[][] copy = new char[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }
        return copy;
    }


    public void resetMap() {
        this.map = deepCopy(originalMap);
    }

    public void restorePellet(int row, int col) {
        if (isInside(row, col) && map[row][col] == '_') {
            map[row][col] = '.';
        }
    }

    // Checks if the tile is within map boundaries
    public boolean isInside(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Checks if the tile is walkable
    public boolean isValidMove(int row, int col) {
        return isInside(row, col) && !(map[row][col] == '#');
    }

    // Checks if a pellet exists at the tile
    public boolean hasPellet(int row, int col) {
        return isInside(row, col) && map[row][col] == '.';
    }

    // Consumes the pellet at the tile
    public void removePellet(int row, int col) {
        if (hasPellet(row, col)) {
            map[row][col] = '_';
        }
    }

    // Gets the tile character
    public char getTile(int row, int col) {
        return isInside(row, col) ? map[row][col] : '#';
    }

    // Gets row count
    public int getRows() {
        return rows;
    }

    // Gets column count
    public int getCols() {
        return cols;
    }

    // Gets map corners
    public Position[] getCorners() {
        return corners;
    }

    // Gets Pac-Man spawn point
    public Position getPlayerStart() {
        return playerStart;
    }

    // Gets Pinky spawn point
    public Position getPinkyStart() {
        return pinkyStart;
    }

    // Gets Inky spawn point
    public Position getInkyStart() {
        return inkyStart;
    }

    // Gets Blinky spawn point
    public Position getBlinkyStart() {
        return blinkyStart;
    }
}
