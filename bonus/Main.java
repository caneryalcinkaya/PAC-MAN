import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    // Target frame rate delay
    private static final int FRAME_DELAY_MS = 16;

    public static void main(String[] args) {
        try {
            // Load map
            MapData mapData = loadMapData("data/map.txt");

            // Init player
            Player player = new Player(mapData.getPlayerStart());

            // Init enemies
            Enemy[] enemies = new Enemy[]{
                    new Pinky(mapData.getPinkyStart()),
                    new Inky(mapData.getInkyStart()),
                    new Blinky(mapData.getBlinkyStart())
            };

            // Init game logic
            Game game = new Game(player, enemies, mapData);

            // Init graphics
            GameRenderer renderer = new GameRenderer(mapData, game);
            renderer.setupDraw();

            // Track previous key states
            boolean lastSpace = false, lastP = false, lastR = false, lastQ = false;
            boolean lastUp = false, lastDown = false, lastLeft = false, lastRight = false;

            // Main game loop
            while (true) {
                long frameStart = System.currentTimeMillis();

                // Read input
                boolean space = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_SPACE);
                boolean p     = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_P);
                boolean r     = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_R);
                boolean q     = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_Q);
                boolean up    = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_UP);
                boolean down  = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_DOWN);
                boolean left  = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_LEFT);
                boolean right = StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_RIGHT);

                // Detect single key presses
                boolean spaceJust = space && !lastSpace;
                boolean pJust     = p     && !lastP;
                boolean rJust     = r     && !lastR;
                boolean qJust     = q     && !lastQ;

                // Pass input to game
                game.handleInput(spaceJust, pJust, rJust, qJust,
                        up, down, left, right);

                // Update logic
                game.update();

                // Draw frame
                renderer.tickAnimation();
                renderer.drawGame();

                // Update key states
                lastSpace = space;
                lastP     = p;
                lastR     = r;
                lastQ     = q;
                lastUp    = up;
                lastDown  = down;
                lastLeft  = left;
                lastRight = right;

                // Control frame rate
                long elapsed = System.currentTimeMillis() - frameStart;
                long sleep = FRAME_DELAY_MS - elapsed;
                if (sleep > 0) {
                    try { Thread.sleep(sleep); } catch (InterruptedException ignored) {}
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Map file could not be loaded.");
            e.printStackTrace();
        }
    }

    // Parses the map text file
    private static MapData loadMapData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        scanner.nextLine();

        String[] playerLine = scanner.nextLine().split(" ");
        Position playerStart = new Position(
                Integer.parseInt(playerLine[1]),
                Integer.parseInt(playerLine[2])
        );

        String[] directEnemyLine = scanner.nextLine().split(" ");
        Position directEnemyStart = new Position(
                Integer.parseInt(directEnemyLine[1]),
                Integer.parseInt(directEnemyLine[2])
        );

        String[] randomChaseEnemyLine = scanner.nextLine().split(" ");
        Position randomChaseEnemyStart = new Position(
                Integer.parseInt(randomChaseEnemyLine[1]),
                Integer.parseInt(randomChaseEnemyLine[2])
        );

        String[] closestCornerEnemyLine = scanner.nextLine().split(" ");
        Position closestCornerEnemyStart = new Position(
                Integer.parseInt(closestCornerEnemyLine[1]),
                Integer.parseInt(closestCornerEnemyLine[2])
        );

        String[] cornerHeader = scanner.nextLine().split(" ");
        int cornerCount = Integer.parseInt(cornerHeader[1]);

        Position[] corners = new Position[cornerCount];
        for (int i = 0; i < cornerCount; i++) {
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            scanner.nextLine();
            corners[i] = new Position(r, c);
        }

        char[][] map = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < cols; j++) {
                map[i][j] = line.charAt(j);
            }
        }

        scanner.close();

        return new MapData(
                map,
                playerStart,
                directEnemyStart,
                randomChaseEnemyStart,
                closestCornerEnemyStart,
                corners
        );
    }
}
