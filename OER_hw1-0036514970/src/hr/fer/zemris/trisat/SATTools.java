package hr.fer.zemris.trisat;

import java.util.Arrays;

public class SATTools {
	
	public static long numberOfTrueClauses(SATFormula formula, BitVector mutVector) {
		return Arrays.stream(formula.getClauses()).filter(e -> e.isSatisfied(mutVector)).count();
	}
	


}
