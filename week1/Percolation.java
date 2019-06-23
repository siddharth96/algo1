import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int BOTTOM_TOP_OVRHEAD = 2;
    private final Cell vTop;
    private final Cell vBottom;
    private final WeightedQuickUnionUF uf;
    private final Cell[][] grid;
    private final int n;
    private int numOpenCell;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size should be >= 1");
        }
        this.uf = new WeightedQuickUnionUF((n * n) + BOTTOM_TOP_OVRHEAD);
        this.grid = new Cell[n][n];
        this.n = n;
        this.vTop = new Cell(n, n);
        this.vBottom = new Cell(n + 1, n + 1);
        this.numOpenCell = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public void open(int row, int col) {
        validateCoord(row, col);
        int r = mapExternalInputToInternal(row);
        int c = mapExternalInputToInternal(col);
        Cell cell = grid[r][c];
        if (!cell.isOpen()) {
            cell.setOpen(true);
            this.numOpenCell++;
            if (isInTopRow(cell)) {
                uf.union(mapXYtoI(cell), mapXYtoI(vTop));
            }
            if (isInBottomRow(cell)) {
                uf.union(mapXYtoI(cell), mapXYtoI(vBottom));
            }
            tryConnectingToAdjacentCell(cell);
        }
    }

    private void validateCoord(int row, int col) {
        if (row <= 0 || row > this.n) {
            throw new IllegalArgumentException("Invalid row coord: " + row);
        }
        else if (col <= 0 || col > this.n) {
            throw new IllegalArgumentException("Invalid col coord: " + col);
        }
    }

    private int mapExternalInputToInternal(int rowOrCol) {
        return rowOrCol - 1;
    }

    private boolean isInTopRow(Cell cell) {
        return cell.getRow() == 0;
    }

    private int mapXYtoI(Cell cell) {
        if (cell.equals(vTop)) {
            // e.g. if n=20 then 0-399 will get used by cell
            // 400 and 401 will be idle for vTop and vBottom
            return this.n * this.n;
        }
        else if (cell.equals(vBottom)) {
            return (this.n * this.n) + 1;
        }
        return (this.n * cell.getRow()) + cell.getCol();
    }

    private boolean isInBottomRow(Cell cell) {
        return cell.getRow() == this.n - 1;
    }

    private boolean isAnyNeighbourRowFull(Cell cell) {
        Neighbour top = getTop(cell);
        if (top.isValid() && isFull(top.getCell())) {
            return true;
        }
        Neighbour left = getLeft(cell);
        if (left.isValid() && isFull(left.getCell())) {
            return true;
        }
        Neighbour right = getRight(cell);
        if (right.isValid() && isFull(right.getCell())) {
            return true;
        }
        // Bottom might not even be needed as this method
        // is only called for bottom-row
        Neighbour bottom = getBottom(cell);
        if (bottom.isValid() && isFull(bottom.getCell())) {
            return true;
        }
        return false;
    }

    private void tryConnectingToAdjacentCell(Cell cell) {
        Neighbour left = getLeft(cell);
        Neighbour top = getTop(cell);
        Neighbour right = getRight(cell);
        Neighbour bottom = getBottom(cell);

        int cellCoord = mapXYtoI(cell);

        if (isAnOpenNeighbour(left, cellCoord)) {
            uf.union(cellCoord, mapXYtoI(left.getCell()));
        }
        if (isAnOpenNeighbour(top, cellCoord)) {
            uf.union(cellCoord, mapXYtoI(top.getCell()));
        }
        if (isAnOpenNeighbour(right, cellCoord)) {
            uf.union(cellCoord, mapXYtoI(right.getCell()));
        }
        if (isAnOpenNeighbour(bottom, cellCoord)) {
            uf.union(cellCoord, mapXYtoI(bottom.getCell()));
        }
    }

    private Neighbour getLeft(Cell cell) {
        return cell.getCol() == 0
               ? Neighbour.invalidNeighbour()
               : Neighbour.validNeighbour(grid[cell.getRow()][cell.getCol() - 1]);
    }

    private Neighbour getTop(Cell cell) {
        return cell.getRow() == 0
               ? Neighbour.invalidNeighbour()
               : Neighbour.validNeighbour(grid[cell.getRow() - 1][cell.getCol()]);
    }

    private Neighbour getRight(Cell cell) {
        return cell.getCol() == this.n - 1
               ? Neighbour.invalidNeighbour()
               : Neighbour.validNeighbour(grid[cell.getRow()][cell.getCol() + 1]);
    }

    private Neighbour getBottom(Cell cell) {
        return cell.getRow() == this.n - 1
               ? Neighbour.invalidNeighbour()
               : Neighbour.validNeighbour(grid[cell.getRow() + 1][cell.getCol()]);
    }

    private boolean isAnOpenNeighbour(Neighbour neighbour, int cell) {
        return neighbour.isValid() && neighbour.getCell().isOpen() &&
                !uf.connected(cell, mapXYtoI(neighbour.getCell()));
    }

    public boolean isOpen(int row, int col) {
        validateCoord(row, col);
        int r = mapExternalInputToInternal(row);
        int c = mapExternalInputToInternal(col);
        return grid[r][c].isOpen();
    }

    public boolean isFull(int row, int col) {
        validateCoord(row, col);
        int r = mapExternalInputToInternal(row);
        int c = mapExternalInputToInternal(col);
        Cell cell = grid[r][c];
        return isFull(cell);
    }

    public int numberOfOpenSites() {
        return numOpenCell;
    }

    public boolean percolates() {
        return uf.connected(mapXYtoI(vTop), mapXYtoI(vBottom));
    }

    private boolean isFull(Cell cell) {
        return uf.connected(mapXYtoI(cell), mapXYtoI(vTop));
    }

    private static class Neighbour {
        private final boolean isValid;
        private final Cell cell;

        private Neighbour(boolean isValid, Cell cell) {
            this.isValid = isValid;
            this.cell = cell;
        }

        private static Neighbour invalidNeighbour() {
            return new Neighbour(false, null);
        }

        private static Neighbour validNeighbour(Cell cell) {
            return new Neighbour(true, cell);
        }

        public boolean isValid() {
            return isValid;
        }

        public Cell getCell() {
            return cell;
        }
    }

    private static class Cell {
        private final int row;
        private final int col;
        private boolean isOpen;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            result = 31 * result + (isOpen ? 1 : 0);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Cell cell = (Cell) obj;

            if (row != cell.row) return false;
            if (col != cell.col) return false;
            if (isOpen != cell.isOpen) return false;

            return true;
        }
    }
}
