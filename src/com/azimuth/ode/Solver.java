package com.azimuth.ode;

/**
 * Interface for solving an Initial Value Problem.
 * @author marco
 *
 */
public interface Solver {

	/**
	 * 
	 * @param problem
	 * @return
	 */
	public InitialValueProblem.Solution solve(InitialValueProblem problem);
	
	/**
	 * 
	 * @return, the size of the step
	 */
	public double getStepSize();
	public void setStepSize(double h);
	
}
