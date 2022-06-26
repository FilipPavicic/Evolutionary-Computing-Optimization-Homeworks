package hr.fer.oer.hw02.optimization;

public interface IFunction {
	
	public int numberOfVariable();
	
	public double solve(double[] point);
	
	public double[] gradient(double[] point);
}
