// Caner Yalcinkaya
// 2024400273
public class Game {

    public enum GameState {
        START_SCREEN,
        READY,
        PLAYING,
        PAUSED,
        WON,
        LOST,
    }

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1), NONE(0, 0);

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() { return dRow; }
        public int getDCol() { return dCol; }
    }

    // Distance threshold for collisions
    private static final double COLLISION_THRESHOLD = 0.75;

    // Ready state duration in ticks
    private static final int READY_TICKS = 60;

    private final Player player;
    private final Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;

    private int readyTickCounter;

    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
        this.readyTickCounter = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public MapData getMapData() {
        return mapData;
    }

    // Processes keyboard input
    public void handleInput(boolean spacePressed, boolean pPressed, boolean rPressed,
                            boolean qPressed, boolean upPressed, boolean downPressed,
                            boolean leftPressed, boolean rightPressed) {
        if (qPressed) {
            System.exit(0);
        }

        switch (gameState) {
            case START_SCREEN:
                if (spacePressed) {
                    transitionToReady();
                }
                break;

            case READY:
                break;

            case PLAYING:
                if (pPressed) {
                    gameState = GameState.PAUSED;
                }
                if (upPressed)    player.setRequestedDirection(Direction.UP);
                if (downPressed)  player.setRequestedDirection(Direction.DOWN);
                if (leftPressed)  player.setRequestedDirection(Direction.LEFT);
                if (rightPressed) player.setRequestedDirection(Direction.RIGHT);
                break;

            case PAUSED:
                if (pPressed) {
                    gameState = GameState.PLAYING;
                }
                break;

            case LOST:
            case WON:
                if (rPressed) {
                    restart();
                }
                break;

            default:
                break;
        }
    }

    // Updates the game state every tick
    public void update() {
        switch (gameState) {
            case READY:
                readyTickCounter++;
                if (readyTickCounter >= READY_TICKS) {
                    gameState = GameState.PLAYING;
                }
                break;

            case PLAYING:
                player.update(mapData);

                for (Enemy enemy : enemies) {
                    enemy.move(player, mapData);
                }

                checkCollisions();
                checkWinCondition();
                break;

            default:
                break;
        }
    }

    // Checks if the player touches any enemy
    private void checkCollisions() {
        double pr = player.getVisualRow();
        double pc = player.getVisualCol();

        for (Enemy enemy : enemies) {
            double er = enemy.getVisualRow();
            double ec = enemy.getVisualCol();
            double dist = Math.sqrt((pr - er) * (pr - er) + (pc - ec) * (pc - ec));
            if (dist < COLLISION_THRESHOLD) {
                gameState = GameState.LOST;
                return;
            }
        }
    }

    // Checks if the level is cleared
    private void checkWinCondition() {
        int rows = mapData.getRows();
        int cols = mapData.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mapData.hasPellet(r, c)) {
                    return;
                }
            }
        }
        gameState = GameState.WON;
    }

    // Prepares the board for a new game
    private void transitionToReady() {
        mapData.resetMap();
        player.reset(mapData.getPlayerStart());
        enemies[0].reset(mapData.getPinkyStart());
        enemies[1].reset(mapData.getInkyStart());
        enemies[2].reset(mapData.getBlinkyStart());
        readyTickCounter = 0;
        gameState = GameState.READY;
    }

    // Restarts the entire game
    private void restart() {
        transitionToReady();
        gameState = GameState.START_SCREEN;
    }
}