package hr.fer.oer.galgoritam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

public class SASEGASA<T> {
	
	OffspringSelection<T> os;
	int startNumVilage;
	double stopFactor;
	
	public SASEGASA(OffspringSelection<T> os,int startNumVilage, double stopFactor) {
		super();
		this.os = os;
		this.startNumVilage = startNumVilage;
		this.stopFactor = stopFactor;
	}


	public T createSolution(List<T> startPopulation, boolean keepBest) {
		for (int i = startNumVilage; i > 0; i--) {
			startPopulation = nextGeneration(startPopulation, i, keepBest);
		}
		return startPopulation.stream().max((e1,e2) -> Double.compare(os.problem.fit(e1), os.problem.fit(e2))).get();
	}
	
	
	
	private List<T> nextGeneration(List<T> parents, int vilageNum, boolean keepBest) {
		List<List<T>> vilages = devideToVilages(parents,vilageNum);
		List<T> children = new ArrayList<T>();
		for (List<T> vilage : vilages) {
			children.addAll(
					os.generateSolution(
							new HashSet<T>(vilage), keepBest, (pool,pop) -> pop + pool > stopFactor * parents.size() / vilageNum
							)
					);
		}
		return children;
	}

	private List<List<T>> devideToVilages(List<T> parents, int vilageNum) {
		int numberOfGreater = parents.size() - (parents.size() / vilageNum) * vilageNum;
		List<List<T>> vilages = new ArrayList<List<T>>();
		int start = 0;
		int end = 0;
		for (int i = 0; i < vilageNum; i++) {
			start = end;
			end += parents.size() / vilageNum;
			if(i < numberOfGreater) end++;
			if(-1 == Main.debug) System.out.println("Start: " + start +", end: " + end);
			vilages.add(parents.subList(start, end));
		}
		return vilages;
	}
}
