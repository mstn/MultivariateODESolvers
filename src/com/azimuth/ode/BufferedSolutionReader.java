package com.azimuth.ode;

import java.util.LinkedList;
import java.util.List;

import com.azimuth.ode.InitialValueProblem.Solution;


/**
 * This class solves an Initial Value Problem, buffering previous solutions.
 *        
 * @author marco
 *
 */
public class BufferedSolutionReader extends SolutionReader{
	
	/**
	 * size of an interval
	 */
	private final static double DELTA = 2.;
	
	
	/**
	 * TODO use a fixed size array
	 */
	private List<double[]> values;
	private double endTime;
	
	/**
	 * This class wraps a generic Solver for IVP problems.
	 * @param problem
	 * @param solver, the wrapped solver
	 */
	public BufferedSolutionReader(InitialValueProblem problem, Solver solver){
		super(problem, solver);
		// create the buffer where values are stored
		values  = new LinkedList<double[]>();
		values.add(initialValues);
		this.endTime = startTime;
	}
	
	/**
	 * create a buffering solver that uses a solution buffer of the specified size
	 * @param problem
	 * @param solver
	 * @param size
	 */
	public BufferedSolutionReader(InitialValueProblem problem, Solver solver, int size){
		super(problem, solver);
		throw new UnsupportedOperationException("not yet supported");
	}
	
	/**
	 * returns a solution at a given time point
	 * @param time
	 * @return
	 */
	public double[] getValues(double time){
		
		if (time <= startTime){
			return initialValues;
		} else {
			if ( time > endTime ){
				double[] initialValues = values.get( values.size() - 1);
				problem.setInitialValue( initialValues );
				problem.setLowerBound(endTime);
				problem.setUpperBound(time + DELTA);
				Solution solution = solver.solve(problem);
				double[][] valuesByTime = solution.getValuesGroupedByTime();
				// skip the first element
				for (int i=1; i<valuesByTime.length; i++){
					double[] val = valuesByTime[i];
					values.add( val );
				}
				endTime = time + DELTA;
			} 
			
			
			int index = (int) Math.round( (time-startTime)/precision );
			return values.get(index);
			
		}
		
	}

}
