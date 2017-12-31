/*------------------------------------------------------------------
 * Compilation: 	 javac Percolation.java
 * Dependencies: WeightedQuickUnionUF.java
 * Author:		 Kevin Sun
 * Since:		 2017 Dec 25
 * Last Update:	 2017 Dec 31
 * 
 * Log:         
 *      2017 Dec 31: Changed union-find identifiers.
 *      
 *                   Changed boolean matrix, it doesn't have
 *                   empty slots anymore.
 *                   
 *                   Create a helper method checkNeighbors().
 *                   
 *                   Fixed the bug of isFull() by doing the union
 *                   of the dummy start to the first row and the union
 *                   of the dummy end to the last row in the open()
 *                   method instead of in the constructor.
 *                     
 *                   Assessment Score: 100  
 *------------------------------------------------------------------*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The Percolation class simulates a system of n * n square sites,
 * all sites are blocked initially, user can open sites and 
 * test if a site is open, if a site is full (i.e.connected to the first row of the system)
 * or if the system percolates with the opened sites.
 * 
 * @author Kevin Sun
 * @since 2017.Dec.25
 * 
 */
public class Percolation {
	
	private WeightedQuickUnionUF grid1;	// for percolates()
	private WeightedQuickUnionUF grid2;	// for isFull() to avoid "backwash"
	
	private boolean[][] sites;			// n * n matrix, 
										
										
	
	private int nbOpen = 0;
	private int start,end;				// dummy sites start and end
	private int size;
	

	/**
	 * Constructor. Creates a n-by-n grid of sites.
	 * @param n is the size of the grid
	 */
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n <= 0");
		}else {
			size = n;
			grid1 = new WeightedQuickUnionUF( n * n + 2 );
			grid2 = new WeightedQuickUnionUF( n * n + 2 );
			sites = new boolean[n][n];
			
			start = grid1.count() - 2;
			end = start + 1;
			
		}
	}
	
	/**
	 * This method opens a site and check if its 4 neighbors are open,
	 * if a neighbor is open, then connect the site to the neighbor. 
	 * @param row is the row of the site 
	 * @param col is the column of the site 
	 */
	public void open(int row, int col) {
		if ( ! isValid(row, col) ) {
			throw new IllegalArgumentException("Invalid row or column");	
		}else {
			if ( ! sites[row - 1][col - 1] ) {
				sites[row - 1][col - 1] = true;
				nbOpen++;		
				
				/*
	             *  Union the first row to the dummy node start.
	             */            
				if(row == 1) {
				    grid1.union(start, xyTo1D(row, col));
				    grid2.union(start, xyTo1D(row, col));
				}
				
				/*
				 *  Union the last row to the dummy node end.
	             *  To avoid "backwash", the last row of grid2
	             *  is not connected to the dummy end.
	             */            
				if(row == size) {
				    grid1.union(end, xyTo1D(row, col));
				}
				checkNeighbors(row, col);
				
			}
		}		
	}
	
	
	/**
	 * This method verifies if a site is open.
	 * @param row is the row of the site 
	 * @param col is the column of the site 
	 * @return if the site is open
	 */
	public boolean isOpen(int row, int col) {
		if ( ! isValid(row, col) ) {
			throw new IllegalArgumentException("Invalid row or column");	
		}else {
			return sites[row - 1][col - 1];
		}		
	}
	
	/**
	 * This method verifies if a site is full(i.e.connected to the first row of the system).
	 * @param row is the row of the site 
	 * @param col is the column of the site 
	 * @return if the site is full
	 */
	public boolean isFull(int row, int col) {
		if ( ! isValid(row, col) ) {
			throw new IllegalArgumentException("Invalid row or column");	
			
		}else {
			int index = xyTo1D(row, col);
			return isOpen(row, col) && grid2.connected(index, start);			
		}
	}
	
	/**
	 * @return the number of the open sites
	 */
	public int numberOfOpenSites() {
		return nbOpen;
	}
	
	
	/**
	 * @return if the system percolates
	 * (if there is a path from the first row to the last row).
	 */
	public boolean percolates() {
		return grid1.connected(start, end);
	}
	

	
	/**
	 * This method converts the row and column of the site 
	 * to its array index
	 * @param row is the row of the site 
	 * @param col is the column of the site 
	 * @return the array index of the site
	 */
	private int xyTo1D(int row, int col) {
		return (row - 1) * size + (col - 1);
	}
	
	/**
	 * @param row is the row of the site 
	 * @param col is the column of the site 
	 * @return if the row and column are valid
	 */
	private boolean isValid(int row, int col) {
		return row >= 1 && row  <= size 
				&& col  >= 1 && col <= size;
				
	}
	
	
	
	/**
	 * Check if the surrounding sites are open, if they are open then
	 * union the site with its neighbors.
	 * @param row is the row of the site 
     * @param col is the column of the site 
	 */
	private void checkNeighbors(int row, int col) {
		// check if the neighbors are open
		if(isValid(row - 1, col) && isOpen(row - 1, col)) {
			grid1.union(xyTo1D(row, col), xyTo1D(row - 1, col));
			grid2.union(xyTo1D(row, col), xyTo1D(row - 1, col));
			
		}
		
		if(isValid(row + 1, col) && isOpen(row + 1, col)) {
			grid1.union(xyTo1D(row, col), xyTo1D(row + 1, col));
			grid2.union(xyTo1D(row, col), xyTo1D(row + 1, col));
			
		}
		
		if(isValid(row, col + 1) && isOpen(row, col + 1)) {
			grid1.union(xyTo1D(row, col), xyTo1D(row, col + 1));
			grid2.union(xyTo1D(row, col), xyTo1D(row, col + 1));
			
		}
		
		if(isValid(row, col - 1) && isOpen(row, col - 1)) {
			grid1.union(xyTo1D(row, col), xyTo1D(row, col - 1));
			grid2.union(xyTo1D(row, col), xyTo1D(row, col - 1));
			
		}
		
		
	}

}
