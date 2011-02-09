package com.azimuth.ode;

import com.azimuth.ode.InitialValueProblem.Solution;

/**
 * This class solves an IVP problem introducing a constant delay.
 * @author marco
 *
 */
public class DelaySolver implements Solver {
	
	private Solver solver;
	private double delay;
	private int meshPoints;
	
	/**
	 * 
	 * @param delay
	 * @param meshPoints, the number of points used for each interval (t, t+delay)
	 * @param solver
	 */
	public DelaySolver(double delay, int meshPoints, Solver solver){
		this.delay = delay;
		this.solver = solver;
		this.meshPoints = meshPoints;
		solver.setStepSize(delay/meshPoints);
	}

	@Override
	public Solution solve(InitialValueProblem problem) {
		// initial time
		final double t0 = problem.getLowerBound();
		// final time
		final double tn = problem.getUpperBound();
		// the derivative
		final Function[] f = problem.getDerivatives();
		
		// number of intervals
		int k = (int) Math.ceil((tn-t0)/delay );
		
		final int numberOfVars = problem.getSize();

		// store a partial solution for interval (t0-tau,t0+k*tau)
		// k+1 = no intervals (counting the first interval in the past)
		final double[][] solution = new double[numberOfVars][ (k+1)*meshPoints + 1 ];		
		// store time steps
		double[] timesteps = new double[ (k+1)*meshPoints + 1 ];
		
		// fill the solution for the past interval
		for (int i=0; i<=meshPoints; i++){
			for (int j=0; j<numberOfVars; j++){
				solution[j][i] = problem.getInitialValue()[j];
			}
		}
		// for every interval (t0+i*tau, t0 + (i+1)*tau) solve a new IVP
		for (int i=0; i<k; i++){
			final int interval = i; 
			// build a new initial value problem
			InitialValueProblem ivp = new InitialValueProblem() {
				@Override
				public Function[] getDerivatives() {
				
					Function[] derivatives = new Function[numberOfVars];
					for (int j=0; j<numberOfVars; j++){
						final int var = j;
						derivatives[var] = new Function() {
							
							@Override
							public double value(double t, double[] values) {
								// find an index for time t-delay
								int id = (int) Math.floor( meshPoints*(1+ (t-delay)/delay) );
								// build a vector of delayed values
								double[] delays = new double[numberOfVars];
								for (int l=0; l<numberOfVars; l++){
									// look up delayed value for variable l
									delays[l] =  solution[l][id];
								}
								// create new vector of params
								double[] params = new double[2*numberOfVars];
								for (int l=0; l<numberOfVars; l++){
									params[l] = values[l];
									params[l+numberOfVars] = delays[l];
								}
 								return f[var].value(t, params);
							}
						};
					}
					return derivatives;
				}
				
				@Override
				public double getUpperBound() {
					return t0 + interval*delay + delay;
				}
				
				@Override
				public double getLowerBound() {
					return t0 + interval*delay; 
				}
				
				@Override
				public double[] getInitialValue() {
					double t = getLowerBound();
					int id = (int) Math.floor( meshPoints*(1+(t/delay)) );
					double[] values = new double[numberOfVars];
					for (int l=0; l<numberOfVars; l++){
						values[l] = solution[l][id];
					}
					return values;
				}
				

			};	
			// solve the subproblem
			Solution subSolution = solver.solve(ivp);
			// where the solution starts for this interval
			int offset = (i+1)* meshPoints;
			// fill new values in 
			for (int j=0; j<=meshPoints; j++){
				double[][] sub = subSolution.getValues();
				timesteps[j+offset] = subSolution.getTimesteps()[j];
				for (int l=0; l<numberOfVars; l++){
					solution[l][j + offset] = sub[l][j];
				}
			}
		}
		
		// returns the solution starting from t0
		double[][] delaySolution = new double[numberOfVars][ solution[0].length -meshPoints ];
		double[] delayTimesteps= new double[ timesteps.length -meshPoints  ];
		for (int l=0; l<numberOfVars; l++){
			System.arraycopy(solution[l], meshPoints, delaySolution[l], 0, solution[l].length-meshPoints);
		}
		System.arraycopy(timesteps, meshPoints, delayTimesteps, 0, timesteps.length-meshPoints);
		
		return new Solution(delayTimesteps, delaySolution);
	}

	@Override
	public double getStepSize() {
		return solver.getStepSize();
	}

	@Override
	public void setStepSize(double h) {
		// FIXME it is ignored!
		solver.setStepSize(h);
		
	}

}
