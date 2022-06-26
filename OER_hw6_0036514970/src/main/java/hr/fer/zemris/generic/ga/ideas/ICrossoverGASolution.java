package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public interface ICrossoverGASolution {
	
	public GASolution<int[]> cross(GASolution<int[]> g1, GASolution<int[]> g2, IRNG random, GrayScaleImage template); 

}
