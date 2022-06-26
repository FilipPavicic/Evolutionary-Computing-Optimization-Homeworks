package hr.fer.oer.gprogramiranje;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import hr.fer.oer.gprogramiranje.node.BinaryNode;
import hr.fer.oer.gprogramiranje.node.Constante;
import hr.fer.oer.gprogramiranje.node.Node;
import hr.fer.oer.gprogramiranje.node.Operators;
import hr.fer.oer.gprogramiranje.node.UnaryNode;
import hr.fer.oer.gprogramiranje.node.Variable;
import hr.fer.oer.tools.IProblem;
import hr.fer.oer.tools.Tools;
import hr.fer.oer.tools.TournamentSelection;
import hr.fer.oer.tools.Tuple;

public class SymbolicRegresionProblem  {

	int maxDeep;
	int maxCrossIteration;
	int populationSize;
	int costEvaluations;
	int tournamentSize;
	int multiplicationVariableChoise;
	int multiplicationConstanteChoise;
	int multiplicationOperatorChoise;
	int numberTheSameBeforeConversation;
	double mutationProbability;
	double crossProbability;
	double reproductionProbability;
	List<String> operators;
	int numberOfVariable;
	double[][] values;
	Properties properties;
	Tuple<Double, Double> constanteBound;
	boolean useLinearScaling;
	Random random = new Random();
	private static int fitCounter = 0;
	private final double EPSILON = 1e-15;
	
	private final Comparator<Tree> PROBLEM_COMPARATOR = (a,b)-> {
		double af = fit(a);
		double bf = fit(b);
		int rez = Double.compare(af,bf);
		if(rez == 0)
			return Integer.compare(a.toString().length(), b.toString().length()) * -1;
		return rez;
	};

	public SymbolicRegresionProblem(String dataPath, String propertiesPath) {
		values = Tools.readVariables(dataPath);
		numberOfVariable = values[0].length - 1; 
		properties = Tools.readProperties(propertiesPath);
		setProperties(properties);
	}
	
	public Tree solveProblem(boolean keepBest) {
		List<Tree> populacija = createRadnomPopulation();
		fitCounter = 0;
		double bestFit = Double.NEGATIVE_INFINITY;
		int counter = 0;
		while(fitCounter < costEvaluations && counter < numberTheSameBeforeConversation) {
			populacija = nextGeneration(populacija, keepBest);
			Tree best = getBest(populacija);
			double bestGenFit = fit(best);
			if(bestGenFit > bestFit) {
				counter = 0;
				bestFit = bestGenFit;
			} else {
				counter++;
			}
			System.out.println("Najbolja formula: "+ best +", njena dobrota: " + fit(best) );
		}
		return getBest(populacija);
	}

	public List<Tree> nextGeneration(List<Tree> parents, boolean keepBest) {
		List<Tree> tmp = new ArrayList<Tree>();
		double rand = random.nextDouble();
		if(keepBest) 
			tmp.add(getBest(parents));
		while(tmp.size() != parents.size()) {
			if(rand < mutationProbability) {
				int i = random.nextInt(parents.size());
				Tree a = parents.get(i).copy();
				mutate(a);
				tmp.add(a);
				continue;
			}
			if(rand < crossProbability) {
				TournamentSelection<Tree> ts = new TournamentSelection<>(new ArrayList<Tree>(parents), PROBLEM_COMPARATOR);
				Tree a = ts.getNext(tournamentSize);
				Tree b = ts.getNext(tournamentSize);
				var tuple = cross(a, b);
				if(parents.size() - tmp.size() == 1) {
					tmp.add(tuple.getFirst());
					continue;
				}
				tmp.add(tuple.getFirst());
				tmp.add(tuple.getSecond());
				continue;
			}
			int i = random.nextInt(parents.size());
			Tree a = parents.get(i).copy();
			tmp.add(a);
			continue;
		}
		return tmp;
	}



	private Tree getBest(List<Tree> parents) {
		return parents.stream().max(PROBLEM_COMPARATOR).get();
	}

	public double fit(Tree solution) {
		fitCounter++;
		if(useLinearScaling) {
			solution.setLinearScalling(1, 0);
			final double tAvg = Stream.of(values).mapToDouble(e -> e[e.length-1]).average().getAsDouble();
			final double yAvg = Stream.of(values).mapToDouble(e -> solution.solve(e)).average().getAsDouble();
			double brojnik = Stream.of(values).mapToDouble((e) -> (solution.solve(e) - yAvg) * (e[e.length-1] - tAvg)).sum();
			double nazivnik = Stream.of(values).mapToDouble(e -> Math.pow(solution.solve(e) - yAvg, 2)).sum();
			double b = brojnik / nazivnik;
			double a = tAvg - b *  yAvg;
			solution.setLinearScalling(b, a);
		}
		double error = Stream.of(values).mapToDouble((e) -> Math.pow(solution.solve(e) - e[e.length-1], 2.0)).sum();
		if(Math.abs(0 - error) < EPSILON)
			return Double.POSITIVE_INFINITY;
			
		Double rez =  1.0 / error * values.length;
		if(rez.equals(Double.NaN)) return Double.NEGATIVE_INFINITY;
		return rez;
	}

	public Tuple<Tree,Tree> cross(Tree first, Tree second) {

		for (int i = 0; i < maxCrossIteration; i++) {
			int indexFirst = random.nextInt(first.size());
			int indexSecond = random.nextInt(second.size());
			int indexDeepFirst = first.getDeepOfNodeIndex(indexFirst);
			int indexDeepSecond = second.getDeepOfNodeIndex(indexSecond);
			Node firstPart = first.getNode(indexFirst);
			Node secondPart = second.getNode(indexSecond);
			if(indexDeepFirst + secondPart.deep() <= maxDeep && indexDeepSecond + firstPart.deep() <= maxDeep) {
				Tree f = first.setNode(indexFirst, secondPart);
				Tree s = second.setNode(indexSecond, firstPart);
				return new Tuple<Tree, Tree>(f, s);
			}
		}
		return new Tuple<Tree, Tree>(first, second);
	}

	public void mutate(Tree tree) {
		int index = random.nextInt(tree.size());
		int indexDeep = tree.getDeepOfNodeIndex(index);
		Node rand = randomNode(maxDeep - indexDeep + 1, false);
		tree.root = tree.setNode(index, rand).root;
	}

	/**
	 * Moguće da metoda kreira malo veću populaciju od size ako (size) % 2(maxDeep-1) nije jednako 0
	 * @param size
	 * @param maxDeep
	 * @return
	 */
	public List<Tree> createRadnomPopulation() {
		List<Tree> lista = new ArrayList<Tree>();
		double chunks = 2.0 * (maxDeep-1);
		int sizeOfEach = (int) Math.ceil(populationSize / chunks);
		for (int i = 2; i <= maxDeep; i++) {
			for (int j = 0; j < sizeOfEach; j++) {
				lista.add(randomTree(i, false));
				lista.add(randomTree(i, true));
			}
		}
		return lista;

	}

	private Tree randomTree(int maxDeep, boolean full) {
		
		return new Tree(randomNode(maxDeep, full));
	}

	public  Node randomNode(int maxDeep, boolean full) {
		Node node = randomTypeNode(maxDeep,full);
		if(node instanceof Variable) return node;
		if(node instanceof Constante) return node;

		Node firstChild = randomNode(maxDeep-1,full);
		if(node instanceof UnaryNode) {
			node.setChildren(firstChild);
			return node;
		}
		Node secondChild = randomNode(maxDeep-1, full);
		node.setChildren(firstChild, secondChild);
		return node;
	}



	private Node randomTypeNode(int maxDeep, boolean full) {
		if(full && maxDeep != 1) {
			return Operators.getRandom(operators, random);
		}
		if(maxDeep == 1) {
			int index = random.nextInt(multiplicationConstanteChoise + multiplicationVariableChoise);
			if(index < multiplicationConstanteChoise) {
				return Constante.randomConstnate(constanteBound, random);
			}
			return Variable.randomOfVariable(numberOfVariable, random);
		}
		int index = random.nextInt(multiplicationConstanteChoise + multiplicationVariableChoise + multiplicationOperatorChoise);

		if(index < multiplicationOperatorChoise) {
			return Operators.getRandom(operators, random);
		}
		index -= multiplicationOperatorChoise;

		if(index <multiplicationVariableChoise) {
			return Variable.randomOfVariable(numberOfVariable, random);
		}
		return Constante.randomConstnate(constanteBound, random);
	}

	private void setProperties(Properties properties) {
		operators = Arrays.asList(properties.getProperty("FunctionNodes").trim().split(", "));
		maxDeep = Integer.parseInt(properties.getProperty("MaxTreeDepth"));
		multiplicationVariableChoise = Integer.parseInt(properties.getProperty("MultiplicationVariableChoise", "1").trim());
		multiplicationConstanteChoise = Integer.parseInt(properties.getProperty("MultiplicationConstanteChoise", "1").trim());
		multiplicationOperatorChoise = Integer.parseInt(properties.getProperty("MultiplicationOperatorChoise", "1").trim());
		String constantRange = properties.getProperty("ConstantRange").trim();

		if(constantRange.equals("N/A")) {
			multiplicationConstanteChoise = 0;
		} else {
			String[] tmp = constantRange.split(",");
			if(tmp.length != 2) throw new IllegalArgumentException("ConstantRange nije dobro definiran");
			double a = Double.parseDouble(tmp[0]);
			double b = Double.parseDouble(tmp[1]);
			constanteBound = new Tuple<Double, Double>(Math.min(a, b), Math.max(a, b));
		}
		populationSize = Integer.parseInt(properties.getProperty("PopulationSize"));
		maxCrossIteration = Integer.parseInt(properties.getProperty("MaxCrossIteration", "20"));
		costEvaluations = Integer.parseInt(properties.getProperty("CostEvaluations"));
		tournamentSize = Integer.parseInt(properties.getProperty("TournamentSize"));

		mutationProbability = Double.parseDouble(properties.getProperty("MutationProbability")); 
		crossProbability = Double.parseDouble(properties.getProperty("CrossProbability"));
		reproductionProbability = 1 - mutationProbability - crossProbability;
		useLinearScaling = Boolean.valueOf(properties.getProperty("UseLinearScaling").trim());
		numberTheSameBeforeConversation = Integer.parseInt(properties.getProperty("NumberTheSameBeforeConversation"));
	}
	
	
}
