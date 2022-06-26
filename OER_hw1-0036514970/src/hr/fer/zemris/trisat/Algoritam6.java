package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Algoritam6 implements IOptAlgorithm {
	
	final static double  NUMBER_CHANGED_VARIABLES = 0.3;

	int maxFlips;
	int maxTries;

	SATFormula formula;

	final Random RANDOM = new Random();

	public Algoritam6(SATFormula formula, int maxFlips, int maxTries) {
		this.formula = formula;
		this.maxFlips = maxFlips;
		this.maxTries = maxTries;

	}



	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		BitVector vector = initial.orElse(new BitVector(RANDOM,formula.getNumberOfVariables()));
		Optional<BitVector> solution = Optional.empty();
		for (int i = 0; i < maxTries; i++) {
			
			
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
			
			System.out.println("Kreæem u novi flip");
			vector = createNewSimilarVector(vector);
		}
		
		System.out.println("Dosegnut maksimaln broj iteracija, zaustavljam pretragu. Vraæam"
				+ " zadnji pronaðen ili Empty ako nije pronaðen");

		return solution;

	}



	private MutableBitVector createNewSimilarVector(BitVector vector) {
		MutableBitVector mutVector = vector.copy();
		Set<Integer> positions = new HashSet<Integer>();
		positions.clear();
		while(positions.size() != formula.getNumberOfVariables() * NUMBER_CHANGED_VARIABLES) {
			positions.add(RANDOM.nextInt(formula.getNumberOfVariables()));
		}
		for (Integer position : positions) {
			mutVector.set(position, !mutVector.get(position));
		}
		return mutVector;
	}







}
