package hr.fer.zemris.optjava.dz5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class MOOP {
	
	private final static Random RANDOM = new Random();

	IMOOPProblem problem;
	int populationSize;
	int iteration;
	int k_selection = 2;
	double mutation_probability = 0.1;
	private int numberOfFront = 0;

	public MOOP(IMOOPProblem problem, int populationSize, int iteration) {
		super();
		this.problem = problem;
		this.populationSize = populationSize;
		this.iteration = iteration;
	}

	public List<double[]> solve() {
		List<Solution> current = createPopulation();
		updateRanks(current);
		List<Solution> next = new ArrayList<>();
		for (int i = 0; i < iteration; i++) {
			long time = System.currentTimeMillis();
			TournamentSelection<Solution> ts = new TournamentSelection<Solution>(current, getComarator(current));
			for (int j = 0; j < populationSize; j++) {
				Solution g1 = ts.getNext(k_selection);
				Solution g2 = ts.getNext(k_selection);
				Solution v = cross(g2,g1);
				mutate(v);
				next.add(v);
				ts.reset();
			}
			current.addAll(next);
			next.clear();
			updateRanks(current);
			int rank = 1;
			current = current.stream().sorted(getComarator(current)).limit(populationSize).collect(Collectors.toList());	
			System.out.println("Iteracija: " + i+ ", potrebno vrijeme: "+ (System.currentTimeMillis() - time) / 1000.0 +", fronte: " + numberOfFront);
		}
		return current.stream().filter(e -> e.rank == 1).map((e) -> e.point).collect(Collectors.toList());
	}

	public void updateRanks(List<Solution> lista) {
		
		List<List<Integer>> dominiraNad = new ArrayList<>(
				IntStream.range(0, lista.size()).boxed().map((i) -> new ArrayList<Integer>()).collect(Collectors.toList())
				);
		
		int[] rangs = new int[lista.size()];
		for (int i = 0; i < lista.size(); i++) {
			for (int j = 0; j < lista.size(); j++) {
				if (i == j) continue;

				if(lista.get(i).isDominate(lista.get(j))) {
					dominiraNad.get(i).add(j);
					rangs[j] ++;
				}
			}
		}
		int updatedRanks = 0;
		int rankValue = 0;
		while(updatedRanks < lista.size()) {
			rankValue ++;
			int[] ranks_tmp = rangs.clone();
			for (int j = 0; j < ranks_tmp.length; j++) {
				if(ranks_tmp[j] == 0) {
					lista.get(j).rank = rankValue;
					rangs[j] = -1;
					for (int d : dominiraNad.get(j)) {
						rangs[d]--;
					}
					updatedRanks ++;
				}
			}

		}
		numberOfFront = rankValue;


	}

	private void mutate(Solution v) {
		for (int i = 0; i < v.point.length; i++) {
			if(RANDOM.nextDouble() > mutation_probability) {
				v.point[i] = problem.randomOnPosition(i);
			}
		}
		v.updateFunValues();

	}

	private Solution cross(Solution g2, Solution g1) {
		double[] tmp = new double[g1.point.length];
		for (int i = 0; i < tmp.length; i++) {
			if(RANDOM.nextDouble() > 0.5) {
				tmp[i] = g1.point[i];
			}
			else {
				tmp[i] = g2.point[i];
			}
		}
		return new Solution(tmp);
	}

	private Comparator<Solution> getComarator(List<Solution> current) {
		List<List<List<Solution>>> sortedGrupe = sortSolutionByFrontValue(current);
		return new Comparator<MOOP.Solution>() {

			@Override
			public int compare(Solution o1, Solution o2) {
				int comp = Integer.compare(o1.rank, o2.rank) ;
				if(comp != 0) return comp;
				return Double.compare(udaljenostGrupa(o1,sortedGrupe), udaljenostGrupa(o2,sortedGrupe)) * -1;
			}

			
		};
	}
	public List<List<List<Solution>>> sortSolutionByFrontValue(List<Solution> current) {
		List<List<List<Solution>>> sortedGrupe = new ArrayList<>();
		
		for (int i = 1; i <= numberOfFront; i++) {
			int r = i;
			List<List<Solution>> tmp = new ArrayList<>();
			for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
				int j1 = j;
				tmp.add(
						current.stream().filter(c -> c.rank == r).
						sorted((s1,s2) -> Double.compare(s1.funValues[j1], s2.funValues[j1])).
						collect(Collectors.toList())
						
				);
			}
			sortedGrupe.add(tmp);
		}
		return sortedGrupe;
	}

	public double udaljenostGrupa(Solution o, List<List<List<Solution>>> sortedGrupe) {
		double d = 0;
		List<List<Solution>> listaFronte = sortedGrupe.get(o.rank -1);
		int i = 0;
		for (List<Solution> lista : listaFronte) {
			int index = lista.indexOf(o);
			if (index == 0 || index == lista.size()-1) return Double.MAX_VALUE;
			d += (lista.get(index+1).funValues[i] - lista.get(index-1).funValues[i]) 
					/ (lista.get(lista.size()-1).funValues[i] - lista.get(0).funValues[i]);
			i++;
		}
		return d;
	}


	private List<Solution> createPopulation() {
		List<Solution> lista = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			lista.add(new Solution(problem.randomSolution()));
		}
		return lista;
	}

	public class Solution {
		public double[] point;
		public double[] funValues;
		

		public int rank = 0;
		public Solution(double[] point) {
			super();
			this.point = point;
			this.funValues = problem.evaluateSolution(point);
		}
		public void updateFunValues() {
			this.funValues = problem.evaluateSolution(point);
		}

		boolean isDominate(Solution o) {
			for (int i = 0; i < this.funValues.length; i++) {
				if(this.funValues[i] > o.funValues[i]) return false;
			}

			for (int i = 0; i < o.funValues.length; i++) {
				if(this.funValues[i] != o.funValues[i]) return true;
			}
			return false;

		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Arrays.hashCode(point);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Solution other = (Solution) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (!Arrays.equals(point, other.point))
				return false;
			return true;
		}
		private MOOP getEnclosingInstance() {
			return MOOP.this;
		}
		
		

	}
}
