package hr.fer.oer.de;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import hr.fer.oer.de.tools.Tools;


public class FunctionParametersProblem implements VectorProblem{
	
	public static final BiFunction<double[], double[], Double> F_4ZAD = (c, x) -> 
	c[0] * x[0] +
	c[1] * Math.pow(x[0],3) * x[1] +
	c[2] * Math.exp(c[3] * x[2]) * (1 + Math.cos(c[4] * x[3])) +
	c[5] * x[3] * Math.pow(x[4],2);

	@Override
	public double fitness(Vector solution) {
		return 1.0 / 
				Stream.of(values)
				.mapToDouble(
						(e) -> Math.pow(function.apply(solution.getArray(), e) - e[e.length-1], 2.0)
				).sum() * values.length;

	}
	
	/**
	 * [x1, x2, x3, x4, x5, y]
	 */
	double values[][];
	int solutionSize;
	BiFunction<double[], double[], Double> function;
	
	public FunctionParametersProblem(BiFunction<double[], double[], Double> function, 
			int solutionSize, double[][] values) {
		this.function = function;
		this.solutionSize = solutionSize;
		this.values = values;
	}
	
	public FunctionParametersProblem(BiFunction<double[], double[], Double> function,
			int solutionSize, String path) {
		this.function = function;
		this.solutionSize = solutionSize;
		this.values = Tools.readMatrix(path);
	}

	@Override
	public int getVectorSize() {
		return solutionSize;
	}

}
