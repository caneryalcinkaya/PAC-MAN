public class Player {
    private static final double VISUAL_SPEED = 0.10;

    private Position pos;
    private int score;

    private Game.Direction currentDirection;
    private Game.Direction requestedDirection;

    private double visualRow;
    private double visualCol;

    private boolean moving;

    public Player(Position pos) {
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.pos = pos;
        this.score = 0;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Game.Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Game.Direction dir) {
        this.currentDirection = dir;
    }

    public Game.Direction getRequestedDirection() {
        return requestedDirection;
    }

    public void setRequestedDirection(Game.Direction dir) {
        this.requestedDirection = dir;
    }

    public double getVisualRow() {
        return visualRow;
    }

    public double getVisualCol() {
        return visualCol;
    }

    public void setVisualRow(double visualRow) {
        this.visualRow = visualRow;
    }

    public void setVisualCol(double visualCol) {
        this.visualCol = visualCol;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Checks if the player reached a grid center
    public boolean isGridAligned() {
        return Math.abs(visualRow - pos.getRow()) < 0.01
                && Math.abs(visualCol - pos.getCol()) < 0.01;
    }

    // Smoothly moves the player sprite
    public void updateVisualPosition() {
        double dRow = pos.getRow() - visualRow;
        double dCol = pos.getCol() - visualCol;

        if (Math.abs(dRow) <= VISUAL_SPEED) {
            visualRow = pos.getRow();
        } else {
            visualRow += Math.signum(dRow) * VISUAL_SPEED;
        }

        if (Math.abs(dCol) <= VISUAL_SPEED) {
            visualCol = pos.getCol();
        } else {
            visualCol += Math.signum(dCol) * VISUAL_SPEED;
        }
    }

    // Updates player movement and pellet collection
    public void update(MapData mapData) {
        // Move only at grid intersections
        if (isGridAligned()) {
            int row = pos.getRow();
            int col = pos.getCol();

            // Try to turn
            int reqRow = row + requestedDirection.getDRow();
            int reqCol = col + requestedDirection.getDCol();

            if (requestedDirection != Game.Direction.NONE && mapData.isValidMove(reqRow, reqCol)) {
                currentDirection = requestedDirection;
            }

            // Move forward
            int nextRow = row + currentDirection.getDRow();
            int nextCol = col + currentDirection.getDCol();

            if (currentDirection != Game.Direction.NONE && mapData.isValidMove(nextRow, nextCol)) {
                pos = new Position(nextRow, nextCol);
                moving = true;

                // Check for pellets
                if (mapData.hasPellet(nextRow, nextCol)) {
                    mapData.removePellet(nextRow, nextCol);
                    score += 10;
                }
            } else {
                // Stop if blocked
                moving = false;
            }
        }

        updateVisualPosition();
    }

    // Sends the player back to start
    public void reset(Position startPos) {
        this.pos = startPos;
        this.visualRow = startPos.getRow();
        this.visualCol = startPos.getCol();
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.score = 0;
        this.moving = false;
    }
}
