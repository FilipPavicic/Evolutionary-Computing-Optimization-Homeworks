package hr.fer.oer.de;

import java.util.Arrays;

public interface Problem<T> {

	public double fitness(T solution);
	
	public default T bestSolution(T[] solution) {
		return Arrays.stream(solution).max((a,b) -> Double.compare(fitness(a), fitness(b))).get();
	}
}
