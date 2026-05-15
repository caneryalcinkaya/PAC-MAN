// Caner Yalcinkaya
// 2024400273
public class Blinky extends Enemy {

    public Blinky(Position pos) {
        super(pos);
    }

    public Blinky(Position pos, BFSPathFinder finder) {
        super(pos, finder);
    }

    // Targets the corner closest to Pac-Man
    @Override
    public Position selectTarget(Player player, MapData mapData) {
        Position[] corners = mapData.getCorners();
        Position playerPos = player.getPos();

        Position closestCorner = null;
        int minDist = Integer.MAX_VALUE;

        for (Position corner : corners) {
            int dist = Math.abs(playerPos.getRow() - corner.getRow())
                    + Math.abs(playerPos.getCol() - corner.getCol());
            if (dist < minDist) {
                minDist = dist;
                closestCorner = corner;
            }
        }

        return closestCorner;
    }
}