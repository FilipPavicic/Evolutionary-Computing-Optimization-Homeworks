package hr.fer.zemris.optjava.dz5.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz5.Function;
import hr.fer.zemris.optjava.dz5.MOOP;
import hr.fer.zemris.optjava.dz5.MOOP.Solution;
import hr.fer.zemris.optjava.dz5.MOOPProblem;

public class TestDominacije {

	public static void main(String[] args) {
		
		double[] a = {6,4};
		double[] b = {5,2};
		double[] c = {4,1};
		double[] d = {3,3};
		double[] e = {2,2};
		List<Solution> lista = new ArrayList<Solution>();

		List<Function<double[]>> fs = new ArrayList<>();
		fs.add((p) -> p[0]);
		fs.add((p) -> p[1]);
		MOOPProblem problem = new MOOPProblem(fs, null);
		MOOP moop = new MOOP(problem, 100, 100);
		lista.add(moop.new Solution(a));
		lista.add(moop.new Solution(b));
		lista.add(moop.new Solution(c));
		lista.add(moop.new Solution(d));
		lista.add(moop.new Solution(e));
		moop.updateRanks(lista);
		for (Solution solution : lista) {
			System.out.println("Solution: " + Arrays.toString(solution.point) +", rank: " + solution.rank);
		}

	}

}
