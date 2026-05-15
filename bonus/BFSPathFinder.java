import java.util.ArrayList;

public class BFSPathFinder {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    // Finds the shortest path from start to goal
    public ArrayList<Position> getFullShortestPath(Position start, Position goal, MapData mapData) {
        if (start.equals(goal)) {
            ArrayList<Position> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        int rows = mapData.getRows();
        int cols = mapData.getCols();

        Position[][] parent = new Position[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        Queue<Position> queue = new Queue<>();
        queue.enqueue(start);
        visited[start.getRow()][start.getCol()] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            Position current = queue.dequeue();

            if (current.equals(goal)) {
                found = true;
                break;
            }

            // Check neighbors in priority order
            for (int i = 0; i < 4; i++) {
                int nr = current.getRow() + DR[i];
                int nc = current.getCol() + DC[i];

                if (mapData.isValidMove(nr, nc) && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = current;
                    queue.enqueue(new Position(nr, nc));
                }
            }
        }

        if (!found) {
            return null;
        }

        // Build the path backwards
        ArrayList<Position> path = new ArrayList<>();
        Position cur = goal;
        while (cur != null && !cur.equals(start)) {
            path.add(0, cur);
            cur = parent[cur.getRow()][cur.getCol()];
        }
        path.add(0, start);

        return path;
    }

    // Gets the first step for the ghost to take
    public Position getNextStep(Position start, Position goal, MapData mapData) {
        ArrayList<Position> path = getFullShortestPath(start, goal, mapData);
        if (path == null || path.size() == 0 || path.size() == 1) {
            return null;
        }
        return path.get(1);
    }
}
