import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation percolation;
    private double[] threshold;
    private double confidence_95 = 1.96;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Vale is out of range");
        }

        threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
            }
            threshold[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(threshold);
    }

    public double stddev() {
        return StdStats.stddev(threshold);
    }

    public double confidenceLo() {
        return mean() - (confidence_95 * stddev()) / Math.sqrt(threshold.length);
    }

    public double confidenceHi() {
        return mean() + (confidence_95 * stddev()) / Math.sqrt(threshold.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.println("%Java PercolationStats " + n + " " + trials);
        StdOut.println("Mean " + percolationStats.mean());
        StdOut.println("stddev " + percolationStats.stddev());
        StdOut.println("95% confidence interval = " + percolationStats.confidenceLo() +
                ", " + percolationStats.confidenceHi());
    }
}
