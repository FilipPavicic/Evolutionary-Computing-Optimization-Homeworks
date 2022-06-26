package hr.fer.zemris.trisat;

import java.util.Optional;
import java.util.Random;

public class Algoritam1 implements IOptAlgorithm {
	
	final int MAX_INTERATION = 1_000_000_000;
	
	SATFormula formula;

	public Algoritam1(SATFormula formula) {
		this.formula = formula;
		
	}



	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector original = initial.orElse(new BitVector(formula.getNumberOfVariables()));
		Optional<BitVector> solution = Optional.empty();
		MutableBitVector vector  = original.copy();
		for (int i = 0; i < MAX_INTERATION; i++) {
			if(formula.isSatisfied(vector)) {
				solution = Optional.of(vector);
				System.out.println(vector);
			}
			vector = increaseVectorByOne(vector);
			if(vector.equals(original)) return solution;
		}
		System.out.println("Dosegnut maksimaln broj iteracija, zaustavljam pretragu. Vraæam"
				+ " zadnji pronaðen ili Empty ako nije pronaðen");
		return solution;
		
	}



	private MutableBitVector increaseVectorByOne(MutableBitVector vector) {
		MutableBitVector mutVector = vector.copy();
		boolean bitToAdd = true;
		for (int i = 0; i < formula.getNumberOfVariables() && bitToAdd; i++) {
			if(vector.get(i) == false) bitToAdd = false;
			mutVector.set(i, !mutVector.get(i));
		}
		// ako je bitVector 1...1 ciklièki vrati 0...0
		if(bitToAdd) return new MutableBitVector(formula.getNumberOfVariables());
		
		return mutVector;
		
	}

}
