import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation percolation;
    private double[] threshold;
    private double trials;
    private int openSites;
    private double confidence_95 = 1.96;

    public PercolationStats(int trials, int n) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Vale is out of range");
        }

        threshold = new double[trials];
        int randx, randy;
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            randx = StdRandom.uniform(1,n + 1);
            randy = StdRandom.uniform(1,n + 1);
            percolation.open(randx, randy);
            openSites = 1;
            while (!percolation.percolates()) {
                randx = StdRandom.uniform(1, n + 1);
                randy = StdRandom.uniform(1, n + 1);

                if (!percolation.isOpen(randx, randy)) {
                    percolation.open(randx, randy);
                    openSites++;
                }
            }

            threshold[i] = ((double) openSites) / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(threshold);
    }

    public double stddev() {
        return StdStats.stddev(threshold);
    }

    public double confidenceLo() {
        return mean() - (confidence_95 * stddev()) / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + (confidence_95 * stddev()) / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int trials, n;
        trials = Integer.parseInt(args[0]);
        n = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(trials, n);

        System.out.println("%Java PercolationStats " + trials + " " + n);
        System.out.println("Mean " + percolationStats.mean());
        System.out.println("stddev " + percolationStats.stddev());
        System.out.println("95% confidence interval = " + percolationStats.confidenceLo() +
                ", " + percolationStats.confidenceHi());
    }
}
