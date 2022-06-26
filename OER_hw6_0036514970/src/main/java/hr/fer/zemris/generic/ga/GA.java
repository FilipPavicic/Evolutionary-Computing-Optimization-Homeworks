package hr.fer.zemris.generic.ga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;
import java.util.function.Function;


import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.ideas.CrossoverGASolutionBasic;
import hr.fer.zemris.generic.ga.ideas.CrossoverGASolutionBasicSmart;
import hr.fer.zemris.generic.ga.ideas.CrossoverGASolutionHalfPart;
import hr.fer.zemris.generic.ga.ideas.CrossoverGASolutionPart;
import hr.fer.zemris.generic.ga.ideas.CrossoverGASolutionPartSmart;
import hr.fer.zemris.generic.ga.ideas.ICrossoverGASolution;
import hr.fer.zemris.generic.ga.ideas.IMutateGaSolution;
import hr.fer.zemris.generic.ga.ideas.IRandomGASolution;
import hr.fer.zemris.generic.ga.ideas.MutateGASolutionBasic;
import hr.fer.zemris.generic.ga.ideas.MutateGASolutionBasicNormalTruncate;
import hr.fer.zemris.generic.ga.ideas.MutateGASolutionPart;
import hr.fer.zemris.generic.ga.ideas.MutateGASolutionPartKiller;
import hr.fer.zemris.generic.ga.ideas.MutateGASolutionPartNormalTruncate;
import hr.fer.zemris.generic.ga.ideas.RandomImageBasic;
import hr.fer.zemris.generic.ga.ideas.RandomImageNormalTruncate;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class GA {
	private static final IRNG GA_RANDOM = new RNGRandomImpl();

	public static int workers = Runtime.getRuntime().availableProcessors();
	private  ThreadLocal<IGAEvaluator<int[]>> threadLocal;
	ConcurrentLinkedQueue<GASolutionWraper<int[]>> clq;
	ConcurrentLinkedQueue<GASolutionWraper<int[]>> clq_out;
	GrayScaleImage template;
	Thread[] radnici;
	int populationSize;
	int iteration;
	int pieces;
	int k_selection;
	boolean keepBest;
	String selectorPreasureType;
	IRandomGASolution randomizer;
	ICrossoverGASolution crossover;
	IMutateGaSolution mutator;
	double probabiltyMutate;
	int debug;
	Double prekid;
	
	public static Map<String, IRandomGASolution> randomCreator = new HashMap<>();
	static {
		randomCreator.put("basic", new RandomImageBasic());
		randomCreator.put("normalTruncate", new RandomImageNormalTruncate());
	}
	
	public static Map<String, ICrossoverGASolution> crossoverCreator = new HashMap<>();
	static {
		crossoverCreator.put("basic", new CrossoverGASolutionBasic());
		crossoverCreator.put("basicSmart", new CrossoverGASolutionBasicSmart());
		crossoverCreator.put("part", new CrossoverGASolutionPart());
		crossoverCreator.put("partSmart", new CrossoverGASolutionPartSmart());
		crossoverCreator.put("halfPart", new CrossoverGASolutionHalfPart());
	}
	
	public static Map<String, IMutateGaSolution> mutateCreator = new HashMap<>();
	static {
		mutateCreator.put("basic", new MutateGASolutionBasic());
		mutateCreator.put("part", new MutateGASolutionPart());
		mutateCreator.put("partNormalTruncate", new MutateGASolutionPartNormalTruncate());
		mutateCreator.put("partKiller", new MutateGASolutionPartKiller());
		mutateCreator.put("basicNormalTruncate", new MutateGASolutionBasicNormalTruncate());
	}
	
	public static Map<String, BiFunction<GASolution<int[]>, GASolution<int[]>[], GASolution<int[]>>> selectorPreasure = new HashMap<>();
	static {
		selectorPreasure.put("none", null);
		selectorPreasure.put("any_better", (s,p) -> {
			if(s.fitness > p[0].fitness || s.fitness > p[1].fitness) return s;
			else {
				if(p[0].fitness > p[1].fitness) return p[0];
				return p[1];
			}
		});
		selectorPreasure.put("all_better", (s,p) -> {
			if(s.fitness > p[0].fitness && s.fitness > p[1].fitness) return s;
			else {
				if(p[0].fitness > p[1].fitness) return p[0];
				return p[1];
			}
		});
		
	}
	

	public GA(GrayScaleImage template, Function<GrayScaleImage, IGAEvaluator<int[]>> evaluatorFunction, Properties properties,int debug, Double prekid) {
		this.template = template;
		this.debug = debug;
		this.prekid = prekid;
		clq = new ConcurrentLinkedQueue<>();
		clq_out = new ConcurrentLinkedQueue<>();
		threadLocal = new ThreadLocal<IGAEvaluator<int[]>>() {
			@Override protected IGAEvaluator<int[]> initialValue() {
				return evaluatorFunction.apply(template);
			}
		};		
		setProperties(properties);
	}
	
	private void setProperties(Properties properties) {
		populationSize = Integer.parseInt(properties.getProperty("populationSize", "200"));
		iteration = Integer.parseInt(properties.getProperty("iteration", "1000"));
		pieces = Integer.parseInt(properties.getProperty("pieces", "200"));
		k_selection = Integer.parseInt(properties.getProperty("k_selection", "3"));
		keepBest = Boolean.parseBoolean(properties.getProperty("keepBest", "true"));
		probabiltyMutate = Double.parseDouble(properties.getProperty("probabiltyMutate", "0.1"));
		randomizer = randomCreator.get(properties.getOrDefault("randomCreator", "basic"));
		crossover = crossoverCreator.get(properties.getOrDefault("crossoverCreator", "basic"));
		mutator = mutateCreator.get(properties.getOrDefault("mutateCreator", "basic"));
		selectorPreasureType = properties.getOrDefault("selectorPreasureType", "none").toString();
		
	}

	public GASolution<int[]> doOptimization() {
		startThreads();
		List<GASolution<int[]>> tmp_lista = new ArrayList<>();
		
		createStartPopulation();
		waitForAllComplete(tmp_lista);
		
		for (int i = 0; i < iteration; i++) {
			
			for (int j = keepBest ? 1:0; j < populationSize; j++) {
				TournamentSelection<GASolution<int[]>> ts = new TournamentSelection<>(tmp_lista, k_selection);
				var g1 = ts.getNext();
				var g2 = ts.getNext();
				var v = crossover.cross(g1, g2,GA_RANDOM,template);
				var v1 = mutator.mutate(v, probabiltyMutate,template.getWidth(), template.getWidth(), GA_RANDOM, 1.0 * i / iteration);
				clq.add(new GASolutionWraper<int[]>(v1,selectorPreasure.get(selectorPreasureType),g1,g2));
			}
			
			//popraviti malo 
			GASolution<int[]> best = tmp_lista.stream().min((e1,e2) -> e1.compareTo(e2)).get();
			if( prekid != null  && best.fitness > prekid) break;
			if(debug > 0 && i %10 == 0) System.out.println("Iteracija: " + i + ", best fitness: " + best.fitness);
			tmp_lista.clear();
			
			if(keepBest) 
				tmp_lista.add(best);
			
			waitForAllComplete(tmp_lista);
			
			//test- možda implementirati
			//probabiltyMutate *=0.998;
		}
		stopThreads();
		return tmp_lista.stream().min((e1,e2) -> e1.compareTo(e2)).get();
			
	}
	


	private void createStartPopulation() {
		for (int i = 0; i < populationSize; i++) {
			GASolution<int[]> image = randomizer.get(pieces, template.getWidth(), template.getWidth(), GA_RANDOM);
			clq.add(new GASolutionWraper<int[]>(image));
		}
	}

	private void waitForAllComplete(List<GASolution<int[]>> tmp_lista) {
		while(tmp_lista.size() < populationSize) {
			GASolutionWraper<int[]> p = null;
			while(p == null) {
				p = clq_out.poll();
			}
			tmp_lista.add(p.getSolution());
		}
	}

	private void startThreads() {
		radnici = new Thread[workers];
		for(int i = 0; i < radnici.length; i++) {
			radnici[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					IGAEvaluator<int[]> evaluator = threadLocal.get();
					while(true) {
						GASolutionWraper<int[]> p = null;
						while(p == null) {
							p = clq.poll();
						}
						if(p==GASolutionWraper.POISON_PILL) break;
						evaluator.evaluate(p.getSolution());
						clq_out.add(p);
					}
				}
			});
		}
		for(int i = 0; i < radnici.length; i++) {
			radnici[i].start();
		}
	}

	private void stopThreads() {
		for(int i = 0; i < radnici.length; i++) {
			clq.add(GASolutionWraper.POISON_PILL);
		}
	}

	static class GASolutionWraper<T>   {

		public static GASolutionWraper POISON_PILL = new GASolutionWraper();

		private GASolution<T> solution;
		private GASolution<T>[] parents = null;
		private BiFunction<GASolution<T>, GASolution<T>[], GASolution<T>> decider = null;

		public GASolutionWraper() {}

		public GASolutionWraper(GASolution<T> solution) {
			this.solution = solution;
		}
		
		public GASolutionWraper(GASolution<T> solution,
				BiFunction<GASolution<T>, GASolution<T>[], GASolution<T>> decider,GASolution<T>...parents) {
			super();
			this.solution = solution;
			this.parents = parents;
			this.decider = decider;
		}

		public GASolution<T> getSolution() {
			if(decider == null ) return solution;
			return decider.apply(solution,parents);
		}

		public void setSolution(GASolution<T> solution) {
			this.solution = solution;
		}

	}
	


}
