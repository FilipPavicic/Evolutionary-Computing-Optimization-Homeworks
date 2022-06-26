package hr.fer.zemris.optjava.dz5;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	static Map<String, IMOOPProblem> mapa = new HashMap<String, IMOOPProblem>();
	static  {
		
		List<Function<double[]>> fs1 = new ArrayList<>();
		fs1.add((p) -> p[0]);
		fs1.add((p) -> (1 + p[1]) / p[0]);
		List<Function<double[]>> fs2 = new ArrayList<>();
		fs2.add((p) -> p[0] * p[0]);
		fs2.add((p) -> p[1] * p[1]);
		fs2.add((p) -> p[2] * p[2]);
		fs2.add((p) -> p[3] * p[3]);
		List<Function<double[]>> fs3 = new ArrayList<>();
		fs3.add((p) -> Math.abs(p[0] * p[0] - p[1] * p[1] - 1));
		fs3.add((p) -> p[0] * p[0]);
		fs3.add((p) -> p[1] * p[1]);
		mapa.put("1", new MOOPProblem(fs1, new BoxConstraint(new double[] {0.1,0}, new double[] {1,5})));
		mapa.put("2", new MOOPProblem(fs2, new BoxConstraint(new double[] {-5,-5,-5,-5}, new double[] {5,5,5,5})));
		mapa.put("3", new MOOPProblem(fs3, new BoxConstraint(new double[] {-5,-5}, new double[] {5,5})));
	}

	public static void main(String[] args) {
		String f = args[0];
		int pop = Integer.parseInt(args[1]);
		int iter = Integer.parseInt(args[2]);

		MOOP moop = new MOOP(mapa.get(f), pop, iter);
		var lista = moop.solve();
		System.out.println("U Prvoj fronti: " +  lista.size());
		String collect = lista.stream().map(e -> Arrays.toString(e).substring(1, Arrays.toString(e).length()-1)).collect(Collectors.joining("\n"));
		System.out.println(collect);
		
		// FileWriter writer = null;
		try(FileWriter writer = new FileWriter("izlaz-dec"+f+".txt")) {
			 writer.write(collect);
			 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		    
	}

}
