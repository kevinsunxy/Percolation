/*------------------------------------------------------------------
 * Compilation: 	 javac PercolationStats.java
 * Execution:	 java PercolationStats gridSize nbTrials 
 * Dependencies: Percolation.java
 * 				 StdRandom.java
 * 				 StdStats.java
 * 
 * Author:		 Kevin Sun
 * Since:		 2017 Dec 30
 * Last Update:	 2017 Dec 31
 * 
 * Log:			
 * 	    2017 Dec 31: Fixed the bug of incorrect standard deviation.
 * 					 I used only one percolation for all trials 
 * 					 in stead of creating a new percolation 
 * 					 for each trial.	
 * 
 *                   Claim percolation as a local variable to reduce
 *                   memory usage.
 *                   
 *                   Create fields mean and stddev to eliminate 
 *                   multiple calls of StdStats methods and reduce 
 *                   time usage.
 *                   
 *                    Assessment Score: 100 
 *------------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * The PercolationStats class calculates the mean value of the percolation threshold,
 * the standard deviation and the 95% confidence interval of a n-by-n grid of square
 * sites. 
 * @author Kevin Sun
 * @since 2017.Dec.31
 *
 */
public class PercolationStats {
	
	private double[] fractions;		// each slot contains the fraction of a trial, 
									// the length of the array is the number of trials
									// fraction = nbOpenSites / nbSites
	private int nbTrials;
	private double mean;
	private double stddev;
	
	/** Constructor. 
	 * @param n is the size of the grid
	 * @param trials is the number of trials 
	 */
	public PercolationStats(int n, int trials) {
		if(n <= 0 || trials <= 0 ) {
			throw new IllegalArgumentException("n or trials <= 0");
		}
		
		nbTrials = trials;
		fractions = new double[nbTrials];
		
		Percolation perc;
		// open random sites until the system percolates
		
		for(int i = 0; i<trials; i++) {
			// new percolation for each trial.
			// I wrote this statement outside of the for loop 
			// and failed the tests in version 1
		   
			perc = new Percolation(n);
			
			while(!perc.percolates()) {
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1;
				perc.open(row, col);
			}
			fractions[i] =   ((double)perc.numberOfOpenSites()) / (n * n) ;
		}
		mean = StdStats.mean(fractions);
		stddev = StdStats.stddev(fractions);
	}
	
	/**
	 * @return the average of fractions
	 */
	public double mean() {		
		return mean;
	}
	
	/**
	 * @return the standard deviation of fractions 
	 */
	public double stddev() {		
		return stddev;		
	}
	
	/**
	 * @return the lower bound of the 95% confidence interval
	 */
	public double confidenceLo() {
		return mean - (1.96 * stddev) / Math.sqrt(nbTrials);	
	}
	
	/**
	 * @return the higher bound of the 95% confidence interval
	 */
	public double confidenceHi() {
		return mean + (1.96 * stddev) / Math.sqrt(nbTrials);			
	}
	
	/**
	 * Calculate and print the mean value of the percolation threshold,
	 * the standard deviation and the 95% confidence interval of a grid of square
	 * sites when run args[1] trials, the size of the grid is args[0].
	 * @param args is the array of input, it contain 2 slots, the first slot is the size 
	 * of the grid, the second is the number of trials
	 */
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		
		PercolationStats ps = new PercolationStats(n, trials);
		System.out.println("mean                    = "+ps.mean());
		System.out.println("stddev                  = "+ps.stddev());		
		System.out.println("95% confidence interval = ["+ps.confidenceLo() + ", " + 
		ps.confidenceHi() + "]" );
		
	}

	
}
