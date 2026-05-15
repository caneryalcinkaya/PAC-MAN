// Caner Yalcinkaya
// 2024400273
public abstract class Enemy {
    protected static final double VISUAL_SPEED = 0.067;

    protected Position pos;
    protected Game.Direction direction;
    protected BFSPathFinder finder;

    protected double visualRow;
    protected double visualCol;

    public Enemy(Position pos) {
        this.pos = pos;
        this.finder = new BFSPathFinder();
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    public Enemy(Position pos, BFSPathFinder finder) {
        this.pos = pos;
        this.finder = finder;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
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

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Game.Direction getDirection() {
        return direction;
    }

    public void setDirection(Game.Direction direction) {
        this.direction = direction;
    }

    // Checks if the enemy is exactly on a grid cell
    public boolean isGridAligned() {
        return Math.abs(visualRow - pos.getRow()) < 0.01
                && Math.abs(visualCol - pos.getCol()) < 0.01;
    }

    // Smoothly moves the enemy sprite
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

    // Finds the direction between two adjacent tiles
    protected Game.Direction getDirectionFromPositions(Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        for (Game.Direction d : Game.Direction.values()) {
            if (d.getDRow() == dr && d.getDCol() == dc) {
                return d;
            }
        }
        return Game.Direction.NONE;
    }

    // Moves the enemy towards its chosen target
    public void move(Player player, MapData mapData) {
        if (isGridAligned()) {
            Position target = selectTarget(player, mapData);
            if (target != null && !target.equals(pos)) {
                Position nextStep = finder.getNextStep(pos, target, mapData);
                if (nextStep != null) {
                    direction = getDirectionFromPositions(pos, nextStep);
                    pos = nextStep;
                }
            }
        }
        updateVisualPosition();
    }

    // Each ghost decides its own target
    public abstract Position selectTarget(Player player, MapData mapData);

    // Sends the enemy back to start
    public void reset(Position startPos) {
        this.pos = startPos;
        this.visualRow = startPos.getRow();
        this.visualCol = startPos.getCol();
        this.direction = Game.Direction.NONE;
    }
}
