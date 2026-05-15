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
        BACKTRACKING // New state for bonus part
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

    private static final double COLLISION_THRESHOLD = 0.75;
    private static final int READY_TICKS = 60;

    private final Player player;
    private final Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;

    private int readyTickCounter;

    // Stack to keep history for backtracking
    private final Stack<Snapshot> historyStack;

    // Initializes the game components
    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
        this.readyTickCounter = 0;
        this.historyStack = new Stack<>();
    }

    public Player getPlayer() { return player; }
    public Enemy[] getEnemies() { return enemies; }
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }
    public MapData getMapData() { return mapData; }

    // Processes keyboard inputs
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
                    // Switch to backtracking when game ends and R is pressed
                    gameState = GameState.BACKTRACKING;
                }
                break;
            default:
                break;
        }
    }

    // Main game logic loop
    public void update() {
        switch (gameState) {
            case READY:
                readyTickCounter++;
                if (readyTickCounter >= READY_TICKS) {
                    gameState = GameState.PLAYING;
                }
                break;

            case PLAYING:
                // Read logical values to save state
                Position oldPlayerPos = player.getPos();
                Game.Direction oldPlayerDir = player.getCurrentDirection();
                int oldScore = player.getScore();

                Position[] oldEnemyPos = new Position[enemies.length];
                Game.Direction[] oldEnemyDir = new Game.Direction[enemies.length];
                for (int i = 0; i < enemies.length; i++) {
                    oldEnemyPos[i] = enemies[i].getPos();
                    oldEnemyDir[i] = enemies[i].getDirection();
                }

                // Update game entities
                player.update(mapData);
                for (Enemy enemy : enemies) {
                    enemy.move(player, mapData);
                }

                // Take a snapshot if anything changed
                boolean stateChanged = !player.getPos().equals(oldPlayerPos);
                for (int i = 0; i < enemies.length; i++) {
                    if (!enemies[i].getPos().equals(oldEnemyPos[i])) {
                        stateChanged = true;
                        break;
                    }
                }

                boolean pelletEaten = player.getScore() > oldScore;
                Position eatenPos = pelletEaten ? player.getPos() : null;

                if (stateChanged || pelletEaten) {
                    historyStack.push(new Snapshot(
                            oldPlayerPos, oldPlayerDir,
                            oldEnemyPos, oldEnemyDir,
                            pelletEaten, eatenPos, oldScore
                    ));
                }

                checkCollisions();
                if (gameState == GameState.PLAYING) {
                    checkWinCondition();
                }
                break;

            // Rewind mechanic
            case BACKTRACKING:
                if (historyStack.isEmpty()) {
                    gameState = GameState.START_SCREEN;
                } else {
                    // Rewind quickly on every tick
                    Snapshot snap = historyStack.pop();

                    // Restore player position directly without smoothing
                    player.setPos(snap.getPlayerPos());
                    player.setCurrentDirection(snap.getPlayerDir());
                    player.setVisualRow(snap.getPlayerPos().getRow());
                    player.setVisualCol(snap.getPlayerPos().getCol());
                    player.setScore(snap.getScore());

                    // Restore enemies
                    Position[] ePos = snap.getEnemyPositions();
                    Game.Direction[] eDir = snap.getEnemyDirections();
                    for (int i = 0; i < enemies.length; i++) {
                        enemies[i].setPos(ePos[i]);
                        enemies[i].setDirection(eDir[i]);
                        enemies[i].setVisualRow(ePos[i].getRow());
                        enemies[i].setVisualCol(ePos[i].getCol());
                    }

                    // Put the pellet back if it was eaten
                    if (snap.isPelletEaten()) {
                        mapData.restorePellet(snap.getPelletPos().getRow(), snap.getPelletPos().getCol());
                    }
                }
                break;

            default:
                break;
        }
    }

    // Checks for player and enemy overlaps
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

    // Checks if all pellets are eaten
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

    // Prepares the board for the next round
    private void transitionToReady() {
        mapData.resetMap();
        player.reset(mapData.getPlayerStart());
        enemies[0].reset(mapData.getPinkyStart());
        enemies[1].reset(mapData.getInkyStart());
        enemies[2].reset(mapData.getBlinkyStart());
        readyTickCounter = 0;
        historyStack.clear(); // Clear the stack
        gameState = GameState.READY;
    }

    // Restarts the game from scratch
    private void restart() {
        transitionToReady();
        gameState = GameState.START_SCREEN;
    }
}