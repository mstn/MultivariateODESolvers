package com.azimuth.ode;

/**
 * This interface represents an Initial Value Problem (IVP) of the form
 * 
 * y' = F(t, y) y(t) = y0
 * 
 * over the interval [t0,tn]
 * 
 * @author marco
 * 
 * 
 * 
 */
public abstract class InitialValueProblem {

	/**
	 * A numerical solution for an IVP
	 * 
	 * @author marco
	 * 
	 */
	public static class Solution {
		/**
		 * An array of array for each variable [[value at time t0, at time t1,
		 * ...],[], ..., [] ]
		 */
		private double[][] values;
		private double[] timesteps;

		public Solution(double[] timesteps, double[][] values) {
			this.values = values;
			this.timesteps = timesteps;
		}

		/**
		 * returns values grouped by variable
		 * 
		 * @return
		 */
		public double[][] getValues() {
			return values;
		}

		public void setValues(double[][] values) {
			this.values = values;
		}

		public double[] getTimesteps() {
			return timesteps;
		}

		public void setTimesteps(double[] timesteps) {
			this.timesteps = timesteps;
		}

		/**
		 * returns the solution arranged in a matrix grouped by time
		 * [[x0,y0][x1,y1], ...]
		 * 
		 * @return
		 */
		public double[][] getValuesGroupedByTime() {
			// calculate the transpose of matrix values
			int numberOfRows = values.length;
			int numberOfColumns = values[0].length;
			double[][] t = new double[numberOfColumns][numberOfRows];
			for (int i = 0; i < numberOfRows; ++i) {
				for (int j = 0; j < numberOfColumns; ++j) {
					t[j][i] = values[i][j];
				}
			}
			return t;
		}

	}

	/**
	 * the lower bound t0
	 */
	private double lowerBound = Double.NaN;

	/**
	 * the upper bound tn
	 */
	private double upperBound = Double.NaN;;

	/**
	 * vector of initial values
	 */
	private double[] initialValues;

	/**
	 * returns the derivative of the IVP
	 * 
	 * F: R x R^n \to R^n
	 * 
	 * as a vector of functions (f1, ..., fn)
	 * 
	 * @return
	 */
	public abstract Function[] getDerivatives();

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public double[] getInitialValue() {
		return initialValues;
	}

	public void setInitialValue(double[] initialValues) {
		this.initialValues = initialValues;
	}

	/**
	 * returns the number of dependent variables
	 * 
	 * @return
	 */
	public int getSize() {
		return getDerivatives().length;
	}

}
