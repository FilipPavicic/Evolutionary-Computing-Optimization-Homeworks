package hr.fer.oer.de;

import java.util.function.BiFunction;

public class Main {

	public static void main(String[] args) {
		var function = FunctionParametersProblem.F_4ZAD;
		FunctionParametersProblem problem = new FunctionParametersProblem(function,6, "zad4_data.txt");
		DifferentialEvolution DE = new DifferentialEvolution("properties.properties");
		Vector best = DE.solve(problem,true);
		System.out.println("Dobrota: " + problem.fitness(best) + "  rješenje " + best);

		

	}

}
