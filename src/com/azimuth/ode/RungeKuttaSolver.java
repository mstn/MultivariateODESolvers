package com.azimuth.ode;


/**
 * A solver for ODE implementing the generic Runge Kutta algorithm
 * 
 * @see http://it.wikipedia.org/wiki/Metodi_di_Runge-Kutta
 * @author marco
 * 
 */
public class RungeKuttaSolver implements Solver{
	
	/**
	 * default constants
	 */
	private final static double DEFAULT_STEP_SIZE = 0.05;
	private final static ButcherTableau DEFAULT_TABLEAU = ButcherTableau.RK4;


	/**
	 * The Butcher Tableau is a matrix of coefficients used in the Runge-Kutta
	 * method
	 * 
	 * @see http://it.wikipedia.org/wiki/Metodi_di_Runge-Kutta
	 * @author marco
	 * 
	 */
	public enum ButcherTableau {

		EULER(new double[] {}, new double[] { 1. }, new double[] { 0. }), 
		TRAPEZOIDAL(
				new double[] { 1. }, new double[] { 0.5, 0.5 },
				new double[] { 1. }), 
	    MIDPOINT(new double[] { 1. / 2. },
				new double[] { 0., 1. / 2. }, new double[] { 0., 1. }), 
		RK4(
				new double[] { 1. / 2., 0., 1. / 2., 0., 0., 1. },
				new double[] { 1. / 6., 1. / 3., 1. / 3., 1. / 6. },
				new double[] { 0, 1. / 2., 1. / 2., 1. });

		private final double[] a;
		private final double[] b;
		private final double[] c;

		ButcherTableau(double[] a, double[] b, double[] c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		public double[] getA() {
			return a;
		}

		public double[] getB() {
			return b;
		}

		public double[] getC() {
			return c;
		}

		public int getStageNumber() {
			return c.length;
		}
	}

	/**
	 * the tableau used to compute the numerical approximation
	 */
	private ButcherTableau tableau;
	/**
	 * the size of the step
	 */
	private double h = Double.NaN;

	/**
	 * Creates an instance of a generic Runge Kutta Solver
	 * 
	 * @param h
	 *            , size step
	 * @param tableau
	 *            , Butcher tableau
	 */
	public RungeKuttaSolver(double h, ButcherTableau tableau) {
		this.h = h;
		this.tableau = tableau;
	}

	/**
	 * Creates an instance of Runge Kutta 4 Solver
	 * 
	 * @param h
	 *            , step size
	 */
	public RungeKuttaSolver(double h) {
		this.h = h;
		this.tableau = DEFAULT_TABLEAU;
	}

	/**
	 * Creates an instance of Runge Kutta 4 Solver
	 */
	public RungeKuttaSolver() {
		this.tableau = DEFAULT_TABLEAU;
		this.h = DEFAULT_STEP_SIZE;
	}

	/**
	 * solves the initial value problem
	 * 
	 * @param problem
	 *            , an instance of an IVP
	 * @return, a numerical solution
	 */
	@Override
	public InitialValueProblem.Solution solve(InitialValueProblem problem) {

		// get parameters from the problem
		double[] initialValues = problem.getInitialValue();
		double lowerBound = problem.getLowerBound();
		double upperBound = problem.getUpperBound();

		int numberOfVars = problem.getSize();
		int numberOfStages = tableau.getStageNumber();

		// the derivative F: R x R^m \to R^m
		Function[] f = problem.getDerivatives();

		// calculate the number of steps
		// since I expect an int, I use "round" to cope with rounding errors
		int numberOfSteps = (int) Math.round((upperBound - lowerBound) / h);

		// columns of the current Butchet Tableau
		double[] c = tableau.getC();
		double[] b = tableau.getB();
		double[] a = tableau.getA();

		// where I store the solution
		double[][] solution = new double[numberOfVars][numberOfSteps + 1];
		double[] times = new double[numberOfSteps + 1];

		// set initial conditions
		for (int m = 0; m < numberOfVars; m++) {
			solution[m][0] = initialValues[m];
		}
		// ... and start time
		times[0] = lowerBound;

		// repeat until the end of the interval
		for (int n = 0; n < numberOfSteps; n++) {
			// create a stage matrix: k[i][j] = i-th stage for variable j
			double[][] k = new double[numberOfStages][numberOfVars];
			// update matrix k
			for (int i = 0; i < numberOfStages; i++) {
				// compute midpoint time
				double t = times[n] + c[i] * h;
				// compute midpoint values
				double[] y = new double[numberOfVars];
				for (int j = 0; j < numberOfVars; j++) {
					for (int s = 0; s < i; s++) {
						y[j] += a[s + i * (i - 1) / 2] * k[s][j];
					}
					y[j] = solution[j][n] + h * y[j];
				}
				// calculate ki for each var
				for (int j=0; j<numberOfVars; j++){
					k[i][j] = f[j].value(t, y);
				}
			}
			// calculate the next approximate value
			for (int j = 0; j < numberOfVars; j++) {
				for (int s = 0; s < numberOfStages; s++) {
					solution[j][n + 1] += b[s] * k[s][j];
				}
				solution[j][n + 1] = solution[j][n] +  h * solution[j][n + 1];
			
			}
			times[n + 1] = lowerBound + (n + 1) * h;
		}

		// return the numerical solution
		return new InitialValueProblem.Solution(times, solution);
	}

	@Override
	public double getStepSize() {
		return h;
	}

	@Override
	public void setStepSize(double h) {
		this.h = h;
		
	}




}
