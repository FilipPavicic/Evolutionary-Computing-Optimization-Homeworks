package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class CrossoverGASolutionPart  implements ICrossoverGASolution {

	@Override
	public GASolution<int[]> cross(GASolution<int[]> g1, GASolution<int[]> g2, IRNG random, GrayScaleImage template) {
		int[] tmp = new int[g1.getData().length];

		for (int i = 0; i < tmp.length; i++) {
			if(i == 0) {		
				if(random.nextDouble() > 0.5) {
					tmp[i] = g1.getData()[i];
					continue;
				}
				tmp[i] = g2.getData()[i];
				continue;
			}
			if(random.nextDouble() > 0.5) {
				tmp[i] = g1.getData()[i];
				tmp[i+1] = g1.getData()[i+1];
				tmp[i+2] = g1.getData()[i+2];
				tmp[i+3] = g1.getData()[i+3];
				tmp[i+4] = g1.getData()[i+4];
			}
			else {
				tmp[i] = g2.getData()[i];
				tmp[i+1] = g2.getData()[i+1];
				tmp[i+2] = g2.getData()[i+2];
				tmp[i+3] = g2.getData()[i+3];
				tmp[i+4] = g2.getData()[i+4];
			}
			i = i+4 ;

		}
		return new GAImageSolution(tmp);
	}

}
