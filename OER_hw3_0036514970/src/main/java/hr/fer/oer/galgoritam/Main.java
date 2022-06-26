package hr.fer.oer.galgoritam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
	
	static int debug = 1;
	
	public static void main(String[] args) {
		
		


		PrijenosnaProblem pp = new PrijenosnaProblem("./APR_lab3_data.txt");
		OffspringSelection<ArrayDoubleSolution> os = new OffspringSelection<ArrayDoubleSolution>(10_000, 0.4, pp, (x) ->  1);
		SASEGASA<ArrayDoubleSolution> sasegasa = new SASEGASA<ArrayDoubleSolution>(os,5, 30);
		Set<ArrayDoubleSolution> set = new HashSet<ArrayDoubleSolution>();
		while(set.size() < 40) set.add(new ArrayDoubleSolution(3, -100, 100));
		sasegasa.createSolution(new ArrayList<ArrayDoubleSolution>(set), true);
	}
}
