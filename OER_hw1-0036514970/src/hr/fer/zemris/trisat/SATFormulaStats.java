package hr.fer.zemris.trisat;

public class SATFormulaStats {
	
	final static int numberOfBest = 2;
	final static double percentageConstantUp=0.01;
	final static double percentageConstantDown=0.1;
	final static double percentageUnitAmount=50;
	
	boolean initialized = false;
	SATFormula formula;
	BitVector assignment;
	double post[];
	
	int numberOfSatisfied;
	boolean satisfied;
	double percentageBonus;
	
	
	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
		this.post = new double[formula.getClauses().length];
	}
	
	// analizira se predano rješenje i pamte svi relevantni pokazatelji
	// primjerice, ažurira elemente polja post[...] ako drugi argument to dozvoli; raèuna Z; ...
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		initialized = true;
		this.assignment = assignment;
		if(updatePercentages) {
			for (int i = 0; i < post.length; i++) {
				if(formula.getClause(i).isSatisfied(assignment)) {
					post[i] += (1 - post[i]) * percentageConstantUp;
					continue;
				}
				post[i] += (0 - post[i]) * percentageConstantDown;
			}
		}
		numberOfSatisfied = (int) SATTools.numberOfTrueClauses(formula, assignment);
		satisfied = formula.isSatisfied(assignment);
		percentageBonus = getNumberOfSatisfied();
		for (int i = 0; i < post.length; i++) {
			int sing = formula.getClause(i).isSatisfied(assignment) ? 1 : -1;
			percentageBonus += sing * percentageUnitAmount * (1- post[i]);
		}
	}
		
	// vraæa temeljem onoga što je setAssignment zapamtio: broj klauzula koje su zadovoljene
	public int getNumberOfSatisfied() {
		if(!initialized) throw new IllegalStateException("BitVector is not initialized."); 
		return numberOfSatisfied;
	}
	// vraæa temeljem onoga što je setAssignment zapamtio
	public boolean isSatisfied() {
		if(!initialized) throw new IllegalStateException("BitVector is not initialized."); 
		return satisfied;
	}
	// vraæa temeljem onoga što je setAssignment zapamtio: suma korekcija klauzula
	// to je korigirani Z iz algoritma 3
	public double getPercentageBonus() {
		if(!initialized) throw new IllegalStateException("BitVector is not initialized."); 
		return percentageBonus;
	}
	
	// vraæa temeljem onoga što je setAssignment zapamtio: procjena postotka za klauzulu
	// to su elementi polja post[...]
	public double getPercentage(int index) {
		return post[index];
	}
	// resetira sve zapamæene vrijednosti na poèetne (tipa: zapamæene statistike)
	public void reset() {
		initialized = false;
		assignment = null;
		this.post = new double[formula.getClauses().length];
		
	}
}