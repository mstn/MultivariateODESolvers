package com.azimuth.ode;

/**
 * This class represent a mathematical function
 * f: R x R^n \to R
 * @author marco
 *
 */
public interface Function {
	
	/**
	 * returns the value of the function 
	 * @param t
	 * @param values
	 * @return
	 */
	public double value(double t, double[] values);

}
