package com.azimuth.test;

import com.azimuth.ode.Function;
import com.azimuth.ode.InitialValueProblem;
import com.azimuth.ode.InitialValueProblem.Solution;
import com.azimuth.ode.RungeKuttaSolver;
import com.azimuth.plot.Plot2D;
import com.azimuth.plot.Plot2D.Series;

// http://mathworld.wolfram.com/Lotka-VolterraEquations.html
public class PredatorPreyTest {
	
	private static final String[] YLABELS = {"prey", "predator"};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialValueProblem model = new InitialValueProblem() {
			
			private final static int PREY = 0;
			private final static int PREDATOR = 1;
			private double preyGrowthRate = 1.5;
			private double impactOnPredation = 1;
			private double predatorDeathRate = 3;
			private double predatorGrowthRate = 1;
			
			private Function[] derivatives = {
					new Function() { // prey rate
						@Override
						public double value(double t, double[] values) {
							double x = values[PREY]; // prey
							double y = values[PREDATOR]; // predator
							return (preyGrowthRate - impactOnPredation*y )*x;
						}
					},
					new Function() { // predator rate
						@Override
						public double value(double t, double[] values) {
							double x = values[PREY]; // prey
							double y = values[PREDATOR]; // predator
							return (-predatorDeathRate + predatorGrowthRate*x )*y;
						}
					}
			};
			

			@Override
			public Function[] getDerivatives() {
				return derivatives;
			}
		};
		model.setInitialValue(new double[]{10, 4});
		model.setLowerBound(0);
		model.setUpperBound(20);
		RungeKuttaSolver solver = new RungeKuttaSolver(0.05);
		Solution solution = solver.solve(model);
		double[][] values = solution.getValues(); 
		Plot2D plot = new Plot2D();
		for (int i=0; i<values.length; i++  ){
			double[] row = values[i];
			Series series = new Series();
			series.setYValues(row);
			series.setXValues(solution.getTimesteps());
			series.setXLabel("time");
			series.setYLabel(YLABELS[i]);
			series.setTitle(YLABELS[i]);
			plot.addSeries(series);
		}
		plot.render("./output/predator_prey");
		
		Plot2D phase = new Plot2D();
		Series series = new Series();
		series.setXValues(values[0]);
		series.setYValues(values[1]);
		phase.addSeries(series);
		phase.render("./output/phase");
		
		
	}

}
