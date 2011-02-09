package com.azimuth.ode;

/**
 * A SolutionReader is a wrapper for an Initial Value Problem Solver
 * @author marco
 *
 */
public abstract class SolutionReader {
	
	protected final Solver solver;
	/**
	 * constants that describe the problem
	 */
	protected final InitialValueProblem problem;
	protected final double[] initialValues;
	protected final double startTime;
	protected final double precision;
	
	/**
	 * creates a wrapper around the solver
	 * @param solver
	 */
	public SolutionReader(InitialValueProblem problem, Solver solver){
		// verify preconditions
		assert( problem.getInitialValue() != null && problem.getInitialValue().length>0);
		assert( !Double.isNaN(problem.getLowerBound()) );
		// store information about the problem
		this.problem = problem;
		this.precision = solver.getStepSize();
		// remember the original initial conditions
		this.initialValues = problem.getInitialValue();
		this.startTime = problem.getLowerBound();
		this.solver = solver;
	}
	
	public SolutionReader(InitialValueProblem problem){
		this(problem, new RungeKuttaSolver());
	}
	
	public SolutionReader(InitialValueProblem problem, double stepSize){
		this(problem, new RungeKuttaSolver(stepSize));
	}
	
	/**
	 * returns the value of the Initial Value Problem at a given time
	 * @param time
	 * @return
	 */
	public abstract double[] getValues(double time);
	
}
