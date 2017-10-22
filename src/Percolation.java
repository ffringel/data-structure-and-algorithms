import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] siteStatus;
    private int count;
    private WeightedQuickUnionUF quickUnion;
    private int n;

    public Percolation(int n) {
        this.n = n;

        siteStatus = new boolean[n][n];
        quickUnion = new WeightedQuickUnionUF((n  * n) + 2);
        count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //initialize site status as blocked
                siteStatus[i][j] = true;

                //initialise site ids
                count++;
                if (i == 0) {
                    quickUnion.union(count, 0);
                } else if (i == n - 1) {
                    quickUnion.union(count, (n * n) + 1);
                }
            }
        }
    }

    public void open(int row, int col) {
        int x, y;
        x = row - 1;
        y = col - 1;
        siteStatus[x][y] = false;
        for (int a = x - 1; a <= x + 1; a += 2) {
            if (a >= 0 && a < n && y < n) {
                if (isOpen(a + 1, y + 1)) {
                    quickUnion.union((a * n) + (y + 1), (x * n) + col);
                }
            }
        }

        for (int b = y - 1; b <= y + 1; b += 2) {
            if (x < n && b >= 0 && b < n) {
                if (isOpen(x + 1, b + 1)) {
                    quickUnion.union((x * n) + (b + 1), (x * n) + col);
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row > 0 && row <= n && col > 0 && col <= n) {
            return !siteStatus[row - 1][col - 1];
        } else {
            throw new IllegalArgumentException("Values are out of range");
        }
    }

    public boolean isFull(int row, int col) {
        if (row > 0 && row <= n && col > 0 && col <= n) {
            return quickUnion.connected(((row - 1) * n) + col, 0) &&
                    !siteStatus[row - 1][col - 1];
        } else {
            throw new IllegalArgumentException("Values are out of range");
        }
    }

    public int numberOfOpenSites() {

        return quickUnion.count();
    }

    public boolean percolates() {
        return quickUnion.connected(0, (n * n) + 1);
    }
}