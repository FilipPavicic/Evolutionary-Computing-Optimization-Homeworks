package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Algoritam3 implements IOptAlgorithm {

	final int MAX_INTERATION = 100_000;

	SATFormula formula;
	SATFormulaStats stats;

	final Random RANDOM = new Random();

	public Algoritam3(SATFormula formula) {
		this.formula = formula;
		this.stats = new SATFormulaStats(formula);

	}



	@Override
	public Optional<BitVector> solve(Optional<BitVector> initial) {
		stats.reset();
		BitVector vector = initial.orElse(new BitVector(RANDOM,formula.getNumberOfVariables()));
		Optional<BitVector> solution = Optional.empty();
		
		List<MutableBitVector> turnVectors = new ArrayList<MutableBitVector>();
		double maxTurnFit = Double.MIN_VALUE;

		for (int i = 0; i < MAX_INTERATION; i++) {
			stats.setAssignment(vector, true);
			if(stats.isSatisfied()) return Optional.of(vector);

			for(MutableBitVector mutVector :new  BitVectorNGenerator(vector).createNeighborhood()) {
				stats.setAssignment(mutVector, false);
				double fit = stats.getPercentageBonus();

				if(fit == maxTurnFit) turnVectors.add(mutVector);

				if(fit > maxTurnFit) {
					turnVectors.clear();
					turnVectors.add(mutVector);
					maxTurnFit = fit;
				}
				
				
			}
			vector = turnVectors.get(RANDOM.nextInt(turnVectors.size()));
			maxTurnFit = Double.MIN_VALUE;
		}
		System.out.println("Dosegnut maksimaln broj iteracija, zaustavljam pretragu. Vraæam"
				+ " zadnji pronaðen ili Empty ako nije pronaðen");

		return solution;

	}
}
