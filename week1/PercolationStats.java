import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int n;
    private final int trials;
    private double[] pThresh;
    private Double cMean;
    private Double cStddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid n or trials");
        }
        this.n = n;
        this.trials = trials;
        this.pThresh = new double[trials];
        this.simulate();
    }

    private void simulate() {
        for (int i = 0; i < this.trials; i++) {
            double ptH = tryPercolate();
            this.pThresh[i] = ptH;
        }
    }

    private double tryPercolate() {
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            percolation.open(row, col);
        }
        return ((double) percolation.numberOfOpenSites()) / ((double) (this.n * this.n));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean                     = " + stats.mean());
        System.out.println("stddev                   = " + stats.stddev());
        System.out.println(
                "95%% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi()
                        + "]");
    }

    public double mean() {
        if (cMean == null) {
            cMean = StdStats.mean(pThresh);
        }
        return cMean;
    }

    public double stddev() {
        if (cStddev == null) {
            cStddev = StdStats.stddev(pThresh);
        }
        return cStddev;
    }

    public double confidenceLo() {
        double avg = mean();
        double var = stddev();
        return avg - ((1.96 * var) / Math.sqrt(trials));
    }

    public double confidenceHi() {
        double avg = mean();
        double var = stddev();
        return avg + ((1.96 * var) / Math.sqrt(trials));
    }
}
