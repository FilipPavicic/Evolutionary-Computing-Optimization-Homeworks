package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SATFormula {

	private int numberOfVariables;
	private Clause clauses[];


	public SATFormula(int numberOfVariables, Clause[] clauses) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses;
	}


	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	public Clause[] getClauses() {
		return clauses;
	}

	public Clause getClause(int index) {
		return clauses[index];
	}
	public boolean isSatisfied(BitVector assignment) {
		return Arrays.stream(clauses).allMatch(c -> c.isSatisfied(assignment));
	}
	@Override
	public String toString() {
		return Arrays.stream(clauses)
				.map(c -> c.toString())
				.collect(Collectors.joining(
						" 0\n", 
						"p cnf "+numberOfVariables+" "+ clauses.length+"\n",
						"%")
						);

	}
}