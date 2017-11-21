import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF quickUnion;
    private WeightedQuickUnionUF backWashChecked;
    private int gridSize;
    private final int startPoint;
    private final int endPoint;
    private boolean[][] openSites;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("the size of the grid should be positive integer");
        }
        quickUnion = new WeightedQuickUnionUF(n * n + 2);
        backWashChecked = new WeightedQuickUnionUF(n * n + 1);

        openSites = new boolean[n][n];
        numberOfOpenSites = 0;
        startPoint = 0;
        endPoint = n * n + 1;
        gridSize = n;
    }

    public void open(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IndexOutOfBoundsException("The specified site is not present in the range");

        if (isOpen(row, col))
            return;
        openSites[row - 1][col - 1] = true;
        numberOfOpenSites++;

        if (row == 1) {
            quickUnion.union(getFlattenId(row, col), startPoint);
            backWashChecked.union(getFlattenId(row, col), startPoint);
        }

        if (row == gridSize) {
            quickUnion.union(getFlattenId(row, col), endPoint);
        }

        if (row > 1 && isOpen(row - 1, col)) {
            quickUnion.union(getFlattenId(row - 1, col), getFlattenId(row, col));
            backWashChecked.union(getFlattenId(row - 1, col), getFlattenId(row, col));
        }

        if (row < gridSize && isOpen(row + 1, col)) {
            quickUnion.union(getFlattenId(row + 1, col), getFlattenId(row, col));
            backWashChecked.union(getFlattenId(row + 1, col), getFlattenId(row, col));
        }

        if (col > 1 && isOpen(row,col - 1)) {
            quickUnion.union(getFlattenId(row, col - 1), getFlattenId(row, col));
            backWashChecked.union(getFlattenId(row, col - 1), getFlattenId(row, col));
        }

        if (col < gridSize && isOpen(row, col + 1)) {
            quickUnion.union(getFlattenId(row, col + 1), getFlattenId(row, col));
            backWashChecked.union(getFlattenId(row, col + 1), getFlattenId(row, col));
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IllegalArgumentException("The specified site is not present in the range");

        return openSites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IllegalArgumentException("The specified site is not present in the range");

        return backWashChecked.connected(getFlattenId(row, col), startPoint);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return numberOfOpenSites() > 0 && quickUnion.connected(startPoint, endPoint);
    }

    private int getFlattenId(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize)
            throw new IndexOutOfBoundsException("The specified site is not present in the range");

        return (row - 1) * gridSize + col;
    }

    public static void main(String[] args) {
        int gridSize = StdIn.readInt();
        int row, col, temp;

        Percolation percolation = new Percolation(gridSize);
        System.out.println(percolation.isFull(1, 1));
        while (!StdIn.isEmpty()) {
            row = StdIn.readInt();
            col = StdIn.readInt();
            percolation.open(row, col);
        }

        System.out.println(percolation.percolates());
        System.out.println(percolation.numberOfOpenSites());
    }
}