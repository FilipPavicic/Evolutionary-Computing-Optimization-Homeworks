package hr.fer.oer.galgoritam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import hr.fer.oer.tools.IProblem;
import hr.fer.oer.tools.TournamentSelection;

public class OffspringSelection<T> {

	int maxGenerationNumber;
	double succChildrenRation;
	IProblem<T> problem;
	ToIntFunction<Integer> CF_function;


	public OffspringSelection(int maxGenerationNumber, double succChildrenRation, IProblem<T> problem,
			ToIntFunction<Integer> cF_function) {
		super();
		this.maxGenerationNumber = maxGenerationNumber;
		this.succChildrenRation = succChildrenRation;
		this.problem = problem;
		CF_function = cF_function;
	}

	public Set<T> generateSolution(Set<T> start, boolean keepBest,BiPredicate<Integer, Integer> stopCondition) {

		int i = 0;
		if(1 >= Main.debug) {
			T best = start.stream().max((e1,e2) -> Double.compare(problem.fit(e1), problem.fit(e2))).get();
			System.out.println("Ušao u iteraciju OS-a, broj elemata: " + start.size());
			System.out.println("Najbolji na početku OS: " + best);
			System.out.println("Njegova dobrora: "+ problem.fit(best));
			
		}
		for (i = 0; i < maxGenerationNumber; i++) {
			Optional<Set<T>> next = nextGeneration(start, keepBest, stopCondition, CF_function.applyAsInt(i));
			if(next.isEmpty()) break;
			
			if(0 >= Main.debug) {
				T best = next.get().stream().max((e1,e2) -> Double.compare(problem.fit(e1), problem.fit(e2))).get();
				System.out.println("Veličina polja: " + next.get().size());
				System.out.println("Najbolji u generaciji: " + best);
				System.out.println("Njegova dobrora: "+ problem.fit(best));
			}
			start = next.get();
		}
		if(1 >= Main.debug) {
			T best = start.stream().max((e1,e2) -> Double.compare(problem.fit(e1), problem.fit(e2))).get();
			System.out.println("Završio OS, broj generacija: " +i);
			System.out.println("Najbolji na kraju OS: " + best);
			System.out.println("Njegova dobrora: "+ problem.fit(best));
		}
		return start;
	}

	/**
	 * Metoda generira novu generaciju, u kojoj ima minimalno SuccChildrenRation uspje�nije djece 
	 * @param parents set roditelja
	 * @param keepTeBest zadr�avamo li najboljeg roditelja u sljede�oj populaciji
	 * @param stopCondition BiPredicate koji na temelju veli�ine pool i pop seta odre�uje jesmo li konvergirali
	 * @param CF Comparison Factor
	 * @return Optional koji sadr�i novu generaciju ako smo je uspijeli generirati
	 */
	private Optional<Set<T>> nextGeneration(Set<T> parents, boolean keepBest, 
			BiPredicate<Integer, Integer> stopCondition, int CF) {
		if(parents.isEmpty()) throw new IllegalArgumentException("Set roditelja ne smije biti prazan");

		Set<T> pop = new HashSet<T>();
		Set<T> pool = new HashSet<T>();
		if(keepBest) 
			pop.add(parents.stream().max((e1,e2) -> Double.compare(problem.fit(e1), problem.fit(e2))).get());

		while(true) {
			if(pop.size() >= succChildrenRation * parents.size() && pop.size() + pool.size() >= parents.size()) {
				if(pop.size() + pool.stream().filter(e -> !pop.contains(e)).count() >= parents.size()) {
					pool.stream().filter(e -> !pop.contains(e)).limit(parents.size() - pop.size()).forEach(e -> pop.add(e));
					return Optional.of(pop);
				}
			}
			if(stopCondition.test(pool.size(), pop.size())) return Optional.empty();
			//if(pool.size() % 10 == 0) System.out.println("pool size: " + pool.size());
			TournamentSelection<T> selection = new TournamentSelection<T>(new ArrayList<T>(parents), (e1,e2) -> Double.compare(problem.fit(e1),problem.fit(e2)));
			T first = selection.getNext(2);
			T second = selection.getNext(2);
			T cross = problem.cross(first,second);
			problem.mutate(cross);
			//System.out.println("cross: "+ problem.fit(cross) + "first: "+ problem.fit(first) +"second: "+ problem.fit(second));
			if(problem.fit(cross) > Math.min(problem.fit(first), problem.fit(second)) + CF * Math.abs(problem.fit(first) - problem.fit(second))) {
				pop.add(cross);
			} else {
				pool.add(cross);
			}

		}
	}




}
