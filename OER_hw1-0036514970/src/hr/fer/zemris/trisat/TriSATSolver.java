package hr.fer.zemris.trisat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TriSATSolver {
	
	private static final int MAX_FLIPS = 100_0;
	private static final int MAX_TRIES = 5;
	

	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Potrebno je upisati toèno 2 argumenta");
			return;
		}
		String algName = args[0];
		String path = args[1];
		
		SATFormula formula = ucitajIzDatoteke(path);
		
		IOptAlgorithm alg = getAlgorithm(algName, formula);
		
		if(alg == null) {
			System.out.println("Algoritam s imenom: " + algName + " nije pronaðen.");
			return;
		}
		
		Optional<BitVector> solution = alg.solve(Optional.empty());
		if(solution.isPresent()) {
			BitVector sol = solution.get();
			System.out.println("Imamo rješenje: " + sol);
		} else {
			System.out.println("Rješenje nije pronaðeno.");


		}

	}

	private static IOptAlgorithm getAlgorithm(String algName, SATFormula formula) {
		return switch (algName) {
		case "1" -> new Algoritam1(formula);
		case "2" -> new Algoritam2(formula);
		case "3" -> new Algoritam3(formula);
		case "4" -> new Algoritam4(formula, MAX_FLIPS,MAX_TRIES);
		case "5" -> new Algoritam5(formula, MAX_FLIPS,MAX_TRIES);
		case "6" -> new Algoritam6(formula, MAX_FLIPS,MAX_TRIES);
		default -> null;
		};
	}

	private static SATFormula ucitajIzDatoteke(String path) {
		int numberOfVariables = 0;
		int numberOfClause = 0;
		Clause clauses[] = null;
		int counter = 0;
		
		Scanner myReader = null;
		try {
			File myObj = new File(path);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				
				if(data.startsWith("c")) continue;
				if(data.startsWith("%")) break;
				
				if(data.startsWith("p")) {
					data = data.replaceAll(" * "," ");
					String parts[] = data.split(" ");
					numberOfVariables = Integer.parseInt(parts[2]);
					numberOfClause = Integer.parseInt(parts[3]);
					clauses = new Clause[numberOfClause];
					counter = 0;
					continue;
				}
				
				String parts[] = data.split(" 0");
				for (String part : parts) {
					if(part.trim().isEmpty()) continue;
					part = part.replaceAll(" * "," ");
					String variables[] = part.split(" ");
					clauses[counter] = new Clause(Arrays.stream(variables).filter(s-> s.length() != 0).mapToInt(Integer::valueOf).toArray());
					counter++;
				}	
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally {
			myReader.close();
		}
		if(numberOfClause != clauses.length) throw new IllegalStateException("Broj navedenih i pronaðenih klauzula se ne podudara");
		return new SATFormula(numberOfVariables, clauses);
	}
	
	
}
