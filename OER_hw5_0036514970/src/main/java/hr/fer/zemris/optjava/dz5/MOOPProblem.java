package hr.fer.zemris.optjava.dz5;

import java.util.List;

public class MOOPProblem implements IMOOPProblem{
	
	List<Function<double[]>> functions;
	BoxConstraint constraint;
	
	

	public MOOPProblem(List<Function<double[]>> functions, BoxConstraint constraint) {
		super();
		this.functions = functions;
		this.constraint = constraint;
	}

	@Override
	public int getNumberOfObjectives() {
		return functions.size();
	}

	@Override
	public double[] evaluateSolution(double[] solution) {
		double[] r = new double[functions.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = functions.get(i).solve(solution);
		}
		return r;
	}

	@Override
	public double[] randomSolution() {
		return constraint.generateRandomPoint();
	}
	@Override
	public double randomOnPosition(int position) {
		return constraint.randomPositionValue(position);
	}
	

}
