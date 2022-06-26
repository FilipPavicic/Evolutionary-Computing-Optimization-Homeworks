package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Algoritam4 implements IOptAlgorithm {

	int maxFlips;
	int maxTries;

	SATFormula formula;

	final Random RANDOM = new Random();

	public Algoritam4(SATFormula formula, int maxFlips, int maxTries) {
		this.formula = formula;
		this.maxFlips = maxFlips;
		this.maxTries = maxTries;

	}



	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		Optional<BitVector> solution = Optional.empty();
		for (int i = 0; i < maxTries; i++) {
			BitVector vector = initial.orElse(new BitVector(RANDOM,formula.getNumberOfVariables()));
			long bestFit = SATTools.numberOfTrueClauses(formula,vector);

			List<MutableBitVector> turnVectors = new ArrayList<MutableBitVector>();
			long maxTurnFit = -1;

			for (int j = 0; j < maxFlips; j++) {
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
		}
		
		System.out.println("Dosegnut maksimaln broj iteracija, zaustavljam pretragu. Vraæam"
				+ " zadnji pronaðen ili Empty ako nije pronaðen");

		return solution;

	}







}
