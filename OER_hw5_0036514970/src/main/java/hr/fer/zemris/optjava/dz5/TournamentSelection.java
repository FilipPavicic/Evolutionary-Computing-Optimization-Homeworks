package hr.fer.zemris.optjava.dz5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class TournamentSelection<T> {
	List<T> solutions;
	List<T> garbage;
	Comparator<T> comparator;

	
	
	public TournamentSelection(List<T> solutions, Comparator<T> comparator) {
		super();
		this.solutions = new ArrayList<T>(solutions);
		this.garbage = new ArrayList<T>();
		this.comparator = comparator;
	}



	public T getNext(int k) {
		if(solutions.isEmpty()) return null;
		Function<Integer, T> f = (e) -> solutions.get(e);
		int index = new Random().ints(k, 0, solutions.size()).boxed().max((e1,e2) -> comparator.compare(f.apply(e1), f.apply(e2))).get();
		T s = solutions.remove(index);
		garbage.add(s);
		return s;
		
	}



	public void reset() {
		solutions.addAll(garbage);
		garbage.clear();
	}

}
