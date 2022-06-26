package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class CrossoverGASolutionBasic  implements ICrossoverGASolution {

	@Override
	public GASolution<int[]> cross(GASolution<int[]> g1, GASolution<int[]> g2, IRNG random, GrayScaleImage template) {
		int[] tmp = new int[g1.getData().length];
		
		for (int i = 0; i < tmp.length; i++) {
			if(random.nextDouble() > 0.5) {
				tmp[i] = g1.getData()[i];
				continue;
			}
			tmp[i] = g2.getData()[i];	
		}
		return new GAImageSolution(tmp);
	}

}
