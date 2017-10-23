import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] openSites;
    private WeightedQuickUnionUF quickUnion;
    private int n, bottom, numberOfOpenSites;
    private int top = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("the size of the grid should be positive integer");
        }
        quickUnion = new WeightedQuickUnionUF(n * n + 2);
        openSites = new int[n][n];
        this.n = n;
        bottom = n * n + 1;
    }

    public void open(int row, int col) {
        checkRange(row, col);
        int index = getIndex(row, col);
        openSites[row - 1][col - 1] = 1;
        numberOfOpenSites++;

        if (row == 1) {
            quickUnion.union(index, top);
        }

        if (row == n) {
            quickUnion.union(index, bottom);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            quickUnion.union(index, getIndex(row, col - 1));
        }

        if (col < n && isOpen(row,col + 1)) {
            quickUnion.union(index, getIndex(row, col + 1));
        }

        if (row > 1 && isOpen(row - 1, col)) {
            quickUnion.union(index, getIndex(row - 1, col));
        }

        if (row < n && isOpen(row + 1, col)) {
            quickUnion.union(index, getIndex(row + 1, col));
        }
    }

    private int getIndex(int row, int col) {
        return col + (row - 1) * n;
    }

    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return openSites[row - 1][col - 1] == 1;
    }

    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return quickUnion.connected(top, getIndex(row , col));
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return quickUnion.connected(top, bottom);
    }

    private void checkRange(int row, int col) {
        if (!(row >= 1 && row <= n && col >= 1 && col <= n)) {
            throw new IllegalArgumentException("The specified site is not present in the range");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();

        Percolation percolation = new Percolation(n);
        System.out.println(percolation.isFull(1, 1));
        while (!in.isEmpty()) {
            int row = in.readInt();
            int col = in.readInt();
            percolation.open(row, col);
        }

        System.out.println(percolation.percolates());
        System.out.println(percolation.numberOfOpenSites());
    }
}