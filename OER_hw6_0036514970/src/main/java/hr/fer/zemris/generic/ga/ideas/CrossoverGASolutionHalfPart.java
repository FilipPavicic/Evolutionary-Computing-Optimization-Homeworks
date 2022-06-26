package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class CrossoverGASolutionHalfPart  implements ICrossoverGASolution {

	@Override
	public GASolution<int[]> cross(GASolution<int[]> g1, GASolution<int[]> g2, IRNG random, GrayScaleImage template) {
		int[] tmp = new int[g1.getData().length];
		
		for (int i = 0; i < tmp.length; i++) {
			if(random.nextDouble() > 0.5) {
				tmp[i] = g1.getData()[i];
				if((i-1) % 5 == 2) {
					tmp[i+1] = g1.getData()[i+1];
					i++;
				}
				continue;
			}
			tmp[i] = g2.getData()[i];	
			if((i-1) % 5 == 2) {
				tmp[i+1] = g1.getData()[i+1];
				i++;
			}
		}
		return new GAImageSolution(tmp);
	}

}
