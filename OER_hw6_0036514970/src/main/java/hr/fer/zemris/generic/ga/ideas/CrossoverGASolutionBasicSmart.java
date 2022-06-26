package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class CrossoverGASolutionBasicSmart  implements ICrossoverGASolution {

	@Override
	public GASolution<int[]> cross(GASolution<int[]> g1, GASolution<int[]> g2, IRNG random, GrayScaleImage template) {
		int[] tmp = new int[g1.getData().length];
		
		for (int i = 0; i < tmp.length; i++) {
			double prob = 0.5;
//			if(i == 0 || (i-1) % 5 == 4) {
//				double a= Math.pow(Math.abs(g1.fitness), 1.0);
//				double b= Math.pow(Math.abs(g2.fitness), 1.0);
//				prob = a / (a + b);
//			}
			
			if((i-1) % 5 == 2 || (i-1) % 5 == 3) {
				double a= Math.pow(g1.getData()[i], 1.7);
				double b= Math.pow(g2.getData()[i], 1.7);
				prob = a / (a + b);
			}
			if(random.nextDouble() > prob) {
				tmp[i] = g1.getData()[i];
				continue;
			}
			tmp[i] = g2.getData()[i];	
		}
		return new GAImageSolution(tmp);
	}

}
