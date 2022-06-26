package hr.fer.oer.de;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import hr.fer.oer.de.tools.Tools;

public class DifferentialEvolution{
	Random random;
	
	double lowerBound;
	double higerBound;
	int populatioSize;
	int iterations;
	Properties properties;
	double F;
	double Cr;
	String[] strategy;
	
	
	public DifferentialEvolution(String propertiesPath) {
		this.random = new Random();
		properties = Tools.readProperties(propertiesPath);
		lowerBound = Double.parseDouble(properties.getProperty("lowerBound", "-1000"));
		higerBound = Double.parseDouble(properties.getProperty("higerBound", "1000"));
		populatioSize = Integer.parseInt(properties.getProperty("populatioSize", "40"));
		iterations = Integer.parseInt(properties.getProperty("iterations", "100"));
		strategy = properties.getProperty("strategry","DE/rand/1/bin").split("/");
		F = Double.parseDouble(properties.getProperty("F", "1"));
		Cr = Double.parseDouble(properties.getProperty("Cr", "0.5"));
		
	}
	public Vector solve(VectorProblem problem, boolean details) {
		Vector[] populations = createPopulation(problem.getVectorSize());
		Vector[] nextPopulations  = new Vector[populatioSize];
		for (int i = 0; i < iterations; i++) {
			for (int j = 0; j < populatioSize; j++) {
				Vector objectiveVector = populations[j];
				Vector baseVector = getBaseVector(populations, problem);
				Vector[] rVectors = createRVectors(populations,baseVector);
				Vector tmp = new Vector(problem.getVectorSize());
				
				for (int z = 0; z < rVectors.length ; z = z + 2) {
					tmp = tmp.add(rVectors[z].sub(rVectors[z+1]));
				}
				tmp = multiplyWithF(tmp);
				Vector mutant = baseVector.add(tmp);
				Vector tryVector = crossOverVector(objectiveVector,mutant);
				boolean isTryChoise = problem.fitness(objectiveVector) <= problem.fitness(tryVector);
				nextPopulations[j] = isTryChoise ? tryVector : objectiveVector;
			}
			populations = nextPopulations.clone();
			nextPopulations = new Vector[populatioSize];
			if (details) {
				Vector best = problem.bestSolution(populations);
				System.out.println("Iteracija: " + i + ", dobrota: " + problem.fitness(best) + "  rješenje " + best);
			}
		}
		return problem.bestSolution(populations);
	}
	
	private Vector multiplyWithF(Vector tmp) {
		return switch(strategy[1]) {
		case "rand" -> tmp.dot(F);
		case "best" -> {
			var v = new Vector(random.doubles(tmp.size()).map(d -> F+0.001*(d-0.5)).toArray());
			yield tmp.dot(v);
		}
		default -> throw new IllegalArgumentException("Neponata metoda izbora rand vektora: " + strategy[1]);
		};
	}
	private Vector crossOverVector(Vector objectiveVector, Vector mutant) {
		switch(strategy[3]) {
		case "bin"-> {
			Vector tmp = new Vector(objectiveVector.size());
			int n = random.nextInt(objectiveVector.size());
			for (int i = 0; i < objectiveVector.size(); i++) {
				if(i == n) {
					tmp.setElement(i, mutant.getElement(i));
					continue;
				}
				tmp.setElement(i, getRandomElementFromVectors(i, mutant, objectiveVector, Cr));
			}
			return tmp;
		}
		case "exp" -> {
			Vector tmp = new Vector(objectiveVector.size());
			int n = random.nextInt(objectiveVector.size());
			tmp.setElement(n, mutant.getElement(n));
			boolean mutantChoise = true;
			for(int i = 1; i < objectiveVector.size(); i++) {
				if (mutantChoise) mutantChoise = Cr > random.nextDouble();
				int index = (n+i) % objectiveVector.size();
				double el = mutantChoise ? mutant.getElement(index) : objectiveVector.getElement(index);
				tmp.setElement(index, el);
			}
			return tmp;
		}
		default -> throw new IllegalArgumentException("Nepoznat naèin križanja: " + strategy[3]);
		}
	}
	
	private double getRandomElementFromVectors(int i, Vector v1, Vector v2, double cr) {
		double r = random.nextDouble();
		if(r >= cr) {
			return v1.getElement(i);
		}
		else 
			return v2.getElement(i);
	}
	private Vector[] createRVectors(Vector[] populations, Vector baseVector) {
		Set<Vector> setVectors = new HashSet<Vector>();
		int n = Integer.parseInt(strategy[2]);
		if(n < 1) throw new IllegalArgumentException("Broj linearnih kombinacija mora biti pozitivan ");
		while(setVectors.size() < n * 2) {
			Vector randVector = populations[random.nextInt(populations.length)];
			if(!randVector.equals(baseVector)) setVectors.add(randVector);
		}
		return setVectors.toArray(new Vector[0]);
	}
	private Vector getBaseVector(Vector[] populations, VectorProblem problem) {
		return switch(strategy[1]) {
		case "rand" -> populations[random.nextInt(populations.length)];
		case "best" -> problem.bestSolution(populations);
		default -> throw new IllegalArgumentException("Neponata metoda izbora rand vektora: " + strategy[1]);
		};
			
		
	}
	private Vector[] createPopulation(int dimenzion) {
		Vector vectors[] = new Vector[populatioSize];
		for (int i = 0; i < vectors.length; i++) {
			vectors[i] = new Vector(dimenzion, random, lowerBound, higerBound);
		}
		return vectors;
	}
	
	
	
}
