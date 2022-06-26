package hr.fer.oer.hw02.optimization;

import java.util.Iterator;

public class PolynomialTwoVariableFunction  implements IFunction{

	double variables[][];


	/**
	 * [[1,x,x^2,...,x^n],
	 *  [y,yx,yx^2,...,yx^n],
	 *  [y^2,y^2x,y^2x^2,...],
	 *  ...
	 *  [y^m,y^mx,...,y^mx^n]]
	 *  
	 *  npr: f(x,y) = 2 + x + 3y + 4xy
	 *  [[2,1],
	 *   [3,4]]
	 * @param variables
	 */
	public PolynomialTwoVariableFunction(double[][] variables) {
		this.variables = variables;
	}

	/**
	 * f(x,y) = a(x-b)^2 + c(y-d)^2
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public PolynomialTwoVariableFunction(double a, double b, double c, double d) {
		double v[][] = {
				{a*b*b + c*d*d,-2*a*b,a},
				{-2*d*c,0,0},
				{c,0,0}
		};
		this.variables = v;
	};



	@Override
	public int numberOfVariable() {
		return 2;
	}

	@Override
	public double solve(double[] point) {
		if(point.length != 2) throw new IllegalArgumentException("Metoda očekuje točno dvije točke");
		double result = 0;
		for (int i = 0; i < variables.length; i++) {
			for (int j = 0; j < variables[i].length; j++) {
				result += variables[i][j] * Math.pow(point[1], i) * Math.pow(point[0],j);
			}
		}
		return result;
	}

	@Override
	public double[] gradient(double[] point) {
		if(point.length != 2) throw new IllegalArgumentException("Metoda očekuje točno dvije točke");
		double ret[] = new double[2];
		for (int i = 0; i < variables.length; i++) {
			for (int j = 0; j < variables[i].length; j++) {
				if(i - 1 >= 0) {
					ret[1] += variables[i][j] * i * Math.pow(point[1], i-1) * Math.pow(point[0],j);
				}
				if(j - 1 >= 0) {
					ret[0] += variables[i][j] * Math.pow(point[1], i) * j * Math.pow(point[0],j -1);
				}
			}
		}
		return ret;
	}

}
