/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int _n;
    private final int _t;
    private int[] _trialResults;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }


        _t = trials;
        _n = n;
        _trialResults = new int[trials];
        int steps;
        for (int i = 0; i < trials; i++) {
            //System.out.println("Starting trial " + i);
            steps = 0;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = 0, col = 0;
                boolean isOpen = true;

                while (isOpen) {
                    row = StdRandom.uniform(_n) + 1; // n=20, nn = 400, Chosen = 127, row = 6
                    col = StdRandom.uniform(_n) + 1; // n=20, nn = 400, Chosen = 127, col = 7
                    // System.out.println(
                    //         "Chosen index : row " + row + " and col " + col);

                    isOpen = perc.isOpen(row, col);
                }
                //System.out.println("Opening row " + row + " and col " + col);
                perc.open(row, col);

                steps++;
            }
            _trialResults[i] = steps;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double percMean = StdStats.mean(_trialResults) / (_n * _n);
        return percMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double percMean = StdStats.stddev(_trialResults) / (_n * _n);
        return percMean;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double confLow = mean() - (1.96 * stddev()) / Math.sqrt(_t);
        return confLow;

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double confHigh = mean() + (1.96 * stddev()) / Math.sqrt(_t);
        return confHigh;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);      // n
        int T = Integer.parseInt(args[1]);          // T

        PercolationStats percStat = new PercolationStats(n, T);
        System.out.println("mean                    = " + percStat.mean());
        System.out.println("stddev                  = " + percStat.stddev());
        System.out.println("95% confidence interval = [" + percStat.confidenceLo() + ", " + percStat
                .confidenceHi() + "]");

    }

}
