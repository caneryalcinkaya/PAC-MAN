// Caner Yalcinkaya
// 2024400273
public class Snapshot {
    private final Position playerPos;
    private final Game.Direction playerDir;

    private final Position[] enemyPositions;
    private final Game.Direction[] enemyDirections;

    private final boolean pelletEaten;
    private final Position pelletPos;
    private final int score;

    // Creates a snapshot of the current game state
    public Snapshot(Position playerPos, Game.Direction playerDir,
                    Position[] enemyPositions, Game.Direction[] enemyDirections,
                    boolean pelletEaten, Position pelletPos, int score) {

        this.playerPos = playerPos;
        this.playerDir = playerDir;

        // Deep copy arrays to prevent reference issues
        this.enemyPositions = new Position[enemyPositions.length];
        this.enemyDirections = new Game.Direction[enemyDirections.length];
        for (int i = 0; i < enemyPositions.length; i++) {
            this.enemyPositions[i] = enemyPositions[i];
            this.enemyDirections[i] = enemyDirections[i];
        }

        this.pelletEaten = pelletEaten;
        this.pelletPos = pelletPos;
        this.score = score;
    }

    // Gets the saved player position
    public Position getPlayerPos() { return playerPos; }

    // Gets the saved player direction
    public Game.Direction getPlayerDir() { return playerDir; }

    // Gets the saved enemy positions
    public Position[] getEnemyPositions() { return enemyPositions; }

    // Gets the saved enemy directions
    public Game.Direction[] getEnemyDirections() { return enemyDirections; }

    // Checks if a pellet was eaten
    public boolean isPelletEaten() { return pelletEaten; }

    // Gets the eaten pellet position
    public Position getPelletPos() { return pelletPos; }

    // Gets the saved score
    public int getScore() { return score; }
}