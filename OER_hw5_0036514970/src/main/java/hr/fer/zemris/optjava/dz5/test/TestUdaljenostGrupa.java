package hr.fer.zemris.optjava.dz5.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz5.Function;
import hr.fer.zemris.optjava.dz5.MOOP;
import hr.fer.zemris.optjava.dz5.MOOP.Solution;
import hr.fer.zemris.optjava.dz5.MOOPProblem;

public class TestUdaljenostGrupa {

	public static void main(String[] args) {
		
		double[] a = {12,1};
		double[] b = {4,4};
		double[] c = {1,6};
		double[] d = {10,2};
		double[] e = {3,5};
		double[] f = {9,3};
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
		lista.add(moop.new Solution(f));
		moop.updateRanks(lista);
		for (Solution solution : lista) {
			System.out.println("Solution: " + Arrays.toString(solution.point) +", rank: " + solution.rank);
		}

		System.out.println("Udaljenost grupiranjem: b(4,4) = " + 
				moop.udaljenostGrupa(lista.get(1), moop.sortSolutionByFrontValue(lista)));
		System.out.println("Udaljenost grupiranjem: e(3,5) = " + 
				moop.udaljenostGrupa(lista.get(4), moop.sortSolutionByFrontValue(lista)));
		System.out.println("Udaljenost grupiranjem: f(9,3) = " + 
				moop.udaljenostGrupa(lista.get(5), moop.sortSolutionByFrontValue(lista)));
		System.out.println("Udaljenost grupiranjem: d(10,2) = " + 
				moop.udaljenostGrupa(lista.get(3), moop.sortSolutionByFrontValue(lista)));

	}

}
