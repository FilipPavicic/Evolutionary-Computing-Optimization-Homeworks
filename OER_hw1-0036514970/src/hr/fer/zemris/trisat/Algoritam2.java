package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Algoritam2 implements IOptAlgorithm {
	
	final int MAX_INTERATION = 100_000;
	
	SATFormula formula;
	
	final Random RANDOM = new Random();

	public Algoritam2(SATFormula formula) {
		this.formula = formula;
		
	}



	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector vector = initial.orElse(new BitVector(RANDOM,formula.getNumberOfVariables()));
		Optional<BitVector> solution = Optional.empty();
		long bestFit = SATTools.numberOfTrueClauses(formula,vector);
		
		
		List<MutableBitVector> turnVectors = new ArrayList<MutableBitVector>();
		long maxTurnFit = -1;
		
		for (int i = 0; i < MAX_INTERATION; i++) {
			if(formula.isSatisfied(vector)) return Optional.of(vector);
			

			for(MutableBitVector mutVector :new  BitVectorNGenerator(vector).createNeighborhood()) {
				long fit = SATTools.numberOfTrueClauses(formula,mutVector);
				
				if(fit == maxTurnFit) turnVectors.add(mutVector);
				
				if(fit > maxTurnFit) {
					turnVectors.clear();
					turnVectors.add(mutVector);
					maxTurnFit = fit;
				}	
			}
			
			if(maxTurnFit < bestFit) return Optional.empty();
			vector = turnVectors.get(RANDOM.nextInt(turnVectors.size()));
			bestFit = maxTurnFit;
			
			turnVectors.clear();
			maxTurnFit = -1;
		}
		System.out.println("Dosegnut maksimaln broj iteracija, zaustavljam pretragu. Vraæam"
				+ " zadnji pronaðen ili Empty ako nije pronaðen");
		
		return solution;
		
	}



	



}
