import java.util.ArrayList;
import java.util.Random;

public class Inky extends Enemy {

    private static final double PLAYER_CHASE_PROBABILITY = 0.60;
    private final Random random;

    // Movement vectors
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    public Inky(Position pos) {
        super(pos);
        this.random = new Random();
    }

    public Inky(Position pos, BFSPathFinder finder) {
        super(pos, finder);
        this.random = new Random();
    }

    // Targets Pac-Man or a random nearby tile
    @Override
    public Position selectTarget(Player player, MapData mapData) {
        if (random.nextDouble() < PLAYER_CHASE_PROBABILITY) {
            return player.getPos();
        } else {
            // Find empty adjacent tiles
            ArrayList<Position> neighbors = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int nr = pos.getRow() + DR[i];
                int nc = pos.getCol() + DC[i];
                if (mapData.isValidMove(nr, nc)) {
                    neighbors.add(new Position(nr, nc));
                }
            }
            if (neighbors.isEmpty()) {
                return player.getPos(); // Safety fallback
            }
            return neighbors.get(random.nextInt(neighbors.size()));
        }
    }
}
