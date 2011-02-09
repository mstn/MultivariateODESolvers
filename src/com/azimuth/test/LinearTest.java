package com.azimuth.test;

import com.azimuth.ode.Function;
import com.azimuth.ode.InitialValueProblem;
import com.azimuth.ode.RungeKuttaSolver;
import com.azimuth.ode.InitialValueProblem.Solution;
import com.azimuth.plot.Plot2D;
import com.azimuth.plot.Plot2D.Series;

public class LinearTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialValueProblem model = new InitialValueProblem() {
			private Function[] derivaties = {
					new Function() {
						
						@Override
						public double value(double t, double[] values) {
							return values[0];
						}
					}
					
			};

			@Override
			public Function[] getDerivatives() {
				return derivaties;
			}
		};
		model.setInitialValue(new double[]{1});
		model.setLowerBound(0);
		model.setUpperBound(3);
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
			plot.addSeries(series);
		}
		plot.render("./output/linear");

	}

}
