import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Board {
    private static final int BLANK = 0;
    private final int[][] tiles;
    private final int manDist;
    private final int hamDist;
    private final boolean isGoal;
    private final Point2D blankPos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.blankPos = blankPos(tiles);
        this.tiles = deepCopy(tiles);
        this.manDist = computeManhattanDistance(tiles);
        this.hamDist = computeHammingDistance(tiles);
        this.isGoal = validateWhetherGoal(tiles);
    }

    private static Point2D blankPos(int[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == BLANK) {
                    return new Point2D(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Unable to find blank");
    }

    private static int[][] deepCopy(int[][] tiles) {
        int[][] newTiles = new int[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            newTiles[i] = new int[tiles[i].length];
            for (int j = 0; j < tiles[i].length; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }
        return newTiles;
    }

    private static int computeManhattanDistance(int[][] tiles) {
        int expectedVal = 1;
        int manDist = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++, expectedVal++) {
                if (tiles[i][j] != BLANK && (tiles[i][j] != expectedVal)) {
                    Point2D expectedPos = expectedPos(tiles[i][j], tiles.length);
                    manDist += Math.abs(i - expectedPos.x()) + Math.abs(j - expectedPos.y());
                }
            }
        }
        return manDist;
    }

    private static int computeHammingDistance(int[][] tiles) {
        int expectedVal = 1;
        int hmDist = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++, expectedVal++) {
                if (tiles[i][j] != BLANK && (tiles[i][j] != expectedVal)) {
                    hmDist++;
                }
            }
        }
        return hmDist;
    }

    private static boolean validateWhetherGoal(int[][] tiles) {
        int expectedVal = 1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++, expectedVal++) {
                if (tiles[i][j] != BLANK && (tiles[i][j] != expectedVal)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Point2D expectedPos(int ele, int n) {
        if (ele == BLANK) {
            return new Point2D(n - 1, n - 1);
        }
        int x = (ele - 1) / n;
        int y = (ele - 1) % n;
        return new Point2D(x, y);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] tiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] tiles = new int[][] { { 1, 0 }, { 2, 3 } };
        // int[][] tiles = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board.isGoal());
        System.out.println(board.twin());
    }

    // number of tiles out of place
    public int hamming() {
        return hamDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoal;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i = 0, j = 0, i2 = 1, j2 = 1;
        if (tiles[i][j] == BLANK) {
            i++;
        }
        else if (tiles[i2][j2] == BLANK) {
            i2--;
        }
        int[][] newTiles = deepCopy(tiles);
        swap(new Point2D(i, j), new Point2D(i2, j2), newTiles);
        return new Board(newTiles);
    }

    private static void swap(Point2D p1, Point2D p2, int[][] tls) {
        int tmp = tls[(int) p1.x()][(int) p1.y()];
        tls[(int) p1.x()][(int) p1.y()] = tls[(int) p2.x()][(int) p2.y()];
        tls[(int) p2.x()][(int) p2.y()] = tmp;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        Board board2 = (Board) y;
        if (board2.dimension() != this.dimension()) {
            return false;
        }
        for (int i = 0; i < board2.dimension(); i++) {
            for (int j = 0; j < board2.tiles[i].length; j++) {
                if (board2.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append(tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            builder.append("\n");
            for (int j = 0; j < tiles[i].length; j++) {
                builder.append(" ").append(tiles[i][j]);
            }
        }
        return builder.toString();
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final List<Point2D> neighborPoints = findNeighboringPoints();
        return () -> new NeighborBoards(blankPos, this.tiles, neighborPoints.iterator());
    }

    private List<Point2D> findNeighboringPoints() {
        Point2D left = new Point2D(blankPos.x() - 1, blankPos.y());
        Point2D right = new Point2D(blankPos.x() + 1, blankPos.y());
        Point2D up = new Point2D(blankPos.x(), blankPos.y() + 1);
        Point2D down = new Point2D(blankPos.x(), blankPos.y() - 1);

        List<Point2D> neighbours = new ArrayList<>();
        for (Point2D p : Arrays.asList(left, right, up, down)) {
            if (isInBounds(p)) {
                neighbours.add(p);
            }
        }
        return neighbours;
    }

    private boolean isInBounds(Point2D p) {
        return p.x() >= 0 && p.x() < dimension() &&
                p.y() >= 0 && p.y() < dimension();
    }

    private static class NeighborBoards implements Iterator<Board> {
        private final Point2D blankPos;
        private final int[][] originalTiles;
        private final Iterator<Point2D> neighborPoints;

        public NeighborBoards(Point2D blankPos,
                              int[][] originalTiles,
                              Iterator<Point2D> neighborPoints) {
            this.blankPos = blankPos;
            this.originalTiles = originalTiles;
            this.neighborPoints = neighborPoints;
        }

        @Override
        public boolean hasNext() {
            return neighborPoints.hasNext();
        }

        @Override
        public Board next() {
            return createNewBoardFrom(neighborPoints.next());
        }

        private Board createNewBoardFrom(Point2D newBlankPos) {
            int[][] newTiles = deepCopy(originalTiles);
            swap(blankPos, newBlankPos, newTiles);
            return new Board(newTiles);
        }
    }
}
