package hr.fer.zemris.generic.ga;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class TournamentSelection<T extends GASolution<int[]>> {
	List<T> solutions;
	int k;

	
	
	public TournamentSelection(Collection<T> solutions, int k) {
		super();
		this.solutions = new ArrayList<>(solutions);
		this.k = k;
	}



	public T getNext() {
		if(solutions.isEmpty()) return null;
		Function<Integer, T> f = (e) -> solutions.get(e);
		int index = new Random().ints(k, 0, solutions.size()).boxed().min((e1,e2) -> solutions.get(e1).compareTo(solutions.get(e2))).get();
		T s = solutions.get(index);
		solutions.remove(index);
		return s;
		
	}

}
