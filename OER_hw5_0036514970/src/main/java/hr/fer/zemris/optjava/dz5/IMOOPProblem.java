package hr.fer.zemris.optjava.dz5;
interface IMOOPProblem {
	int getNumberOfObjectives();
	double[] evaluateSolution(double[] solution);
	double[] randomSolution();
	double randomOnPosition(int position);
}
