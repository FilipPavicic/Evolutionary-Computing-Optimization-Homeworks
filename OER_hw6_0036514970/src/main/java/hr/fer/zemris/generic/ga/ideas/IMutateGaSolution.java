package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public interface IMutateGaSolution {

	public GASolution<int[]> mutate(GASolution<int[]> v, double probabilty,int width, int height, IRNG random, double stage);
}
