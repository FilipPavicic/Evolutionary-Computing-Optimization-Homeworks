package hr.fer.zemris.generic.ga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GA.GASolutionWraper;
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
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class GA2 {

	public static int workers = Runtime.getRuntime().availableProcessors();
	
	
	private  ThreadLocal<IGAEvaluator<int[]>> threadLocal;
	ConcurrentLinkedQueue<GASolution<int[]>> clqTrue;
	ConcurrentLinkedQueue<GASolution<int[]>> clqFalse;
	ConcurrentLinkedQueue<CreateChildrenJob> jobs;
	GrayScaleImage template;
	Thread[] radnici;
	int populationSize;
	int iteration;
	int pieces;
	int k_selection;
	boolean keepBest;
	String selectorPreasureType;
	static IRandomGASolution randomizer;
	ICrossoverGASolution crossover;
	IMutateGaSolution mutator;
	double probabiltyMutate;
	int debug;
	static private boolean fliper = false;
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
	
	final private Function<Integer,GASolution<int[]>> RANDOM_CREATOR = (i) -> {
		return randomizer.get(pieces, template.getWidth(), template.getWidth(), RNG.getRNG());
	};
	
	final private Function<Integer,GASolution<int[]>> POPULATION_CREATOR = (i) -> {
		TournamentSelection<GASolution<int[]>> ts = new TournamentSelection<>(currentList() , k_selection);
		var g1 = ts.getNext();
		var g2 = ts.getNext();
		var v = crossover.cross(g1, g2,RNG.getRNG(),template);
		var v1 = mutator.mutate(v, probabiltyMutate,template.getWidth(), template.getWidth(), RNG.getRNG(), 1.0 * i / iteration);
		return v1;
	};
	

	public GA2(GrayScaleImage template, Function<GrayScaleImage, IGAEvaluator<int[]>> evaluatorFunction, Properties properties,int debug, Double prekid) {
		this.template = template;
		this.debug = debug;
		this.prekid = prekid;
		clqTrue = new ConcurrentLinkedQueue<>();
		clqFalse = new ConcurrentLinkedQueue<>();
		jobs = new ConcurrentLinkedQueue<>();
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
		waitForAllComplete();
		
		for (int i = 0; i < iteration; i++) {
			
			GASolution<int[]> best = currentList().stream().min((e1,e2) -> e1.compareTo(e2)).get();
			if( prekid != null  && best.fitness > prekid) break;
			if(debug > 0 && i %10 == 0) System.out.println("Iteracija: " + i + ", best fitness: " + best.fitness);
			
			if(keepBest) 
				nextList().add(best);
			
			nextPopulation(i);
			waitForAllComplete();
			
			
			
		}
		stopThreads();
		return currentList().stream().min((e1,e2) -> e1.compareTo(e2)).get();
			
	}
	


	private ConcurrentLinkedQueue<GASolution<int[]>> currentList() {
		return fliper ? clqTrue : clqFalse;
	}
	
	private ConcurrentLinkedQueue<GASolution<int[]>> nextList() {
		return fliper ? clqFalse : clqTrue;
	}

	private void nextPopulation(int stage) {
		int pop = (populationSize - (keepBest ? 1 : 0));
		int ostatak = pop % workers;
		for (int i = 0; i < workers; i++) {
			if(i < ostatak) {
				jobs.add(new CreateChildrenJob(pop / workers +1,POPULATION_CREATOR,stage));
				continue;
			}
			
			jobs.add(new CreateChildrenJob(pop / workers, POPULATION_CREATOR,stage));
			
		}
	}

	private void createStartPopulation() {
		int ostatak = populationSize % workers;
		for (int i = 0; i < workers; i++) {
			if(i < ostatak) {
				jobs.add(new CreateChildrenJob((populationSize / workers) + 1, RANDOM_CREATOR,0));
				continue;
			}
			
			jobs.add(new CreateChildrenJob(populationSize / workers, RANDOM_CREATOR,0));
			
		}
	}

	private void waitForAllComplete() {
		while(nextList().size() < populationSize) {
		}

		currentList().clear();
		fliper = !fliper;
	}

	private void startThreads() {
		radnici = new EVOThread[workers];
		for(int i = 0; i < radnici.length; i++) {
			radnici[i] = new EVOThread(new Runnable() {
				@Override
				public void run() {
					IGAEvaluator<int[]> evaluator = threadLocal.get();
					while(true) {
						CreateChildrenJob p = null;
						while(p == null) {
							p = jobs.poll();
						}
						if(p==CreateChildrenJob.POISON_PILL) break;
						
						for (int j = 0; j < p.getChildrenCount(); j++) {
							GASolution<int[]> solution = p.getCreator().apply(p.getStage());
							evaluator.evaluate(solution);
							nextList().add(solution);
							
						}
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
			jobs.add(CreateChildrenJob.POISON_PILL);
		}
	}

	static class CreateChildrenJob   {

		public static CreateChildrenJob POISON_PILL = new CreateChildrenJob();
		

		private int childrenCount;
		private Function<Integer,GASolution<int[]>> creator;
		Integer stage;
		
		private CreateChildrenJob() {}

		public CreateChildrenJob(int childrenCount,Function<Integer,GASolution<int[]>> creator, Integer stage) {
			super();
			this.childrenCount = childrenCount;
			this.creator = creator;
			this.stage = stage;
		}

		public int getChildrenCount() {
			return childrenCount;
		}

		public Function<Integer, GASolution<int[]>> getCreator() {
			return creator;
		}

		public Integer getStage() {
			return stage;
		}

		public void setStage(Integer stage) {
			this.stage = stage;
		}
		
		
		
		
		
		

	}


}
