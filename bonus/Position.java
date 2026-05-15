// Caner Yalcinkaya
// 2024400273
public class Position {
    private final int row;
    private final int col;

    // Sets row and column
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Gets the row
    public int getRow() {
        return row;
    }

    // Gets the column
    public int getCol() {
        return col;
    }

    // Checks if two positions are exactly the same
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Position other = (Position) o;
        return row == other.row && col == other.col;
    }
}