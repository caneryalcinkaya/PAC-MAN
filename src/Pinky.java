public class Pinky extends Enemy {

    public Pinky(Position pos) {
        super(pos);
    }

    public Pinky(Position pos, BFSPathFinder finder) {
        super(pos, finder);
    }

    // Directly targets Pac-Man
    @Override
    public Position selectTarget(Player player, MapData mapData) {
        return player.getPos();
    }
}
