package hr.fer.oer.gprogramiranje;



public class Main {

	public static void main(String[] args) {
		SymbolicRegresionProblem srp = new SymbolicRegresionProblem("./03-GP-podaci/f3.txt", "./properties.properties");
		srp.solveProblem(true);
	}

}
