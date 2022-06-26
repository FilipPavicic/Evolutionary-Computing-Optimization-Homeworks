package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class MutateGASolutionPart implements IMutateGaSolution {

	@Override
	public GASolution<int[]> mutate(GASolution<int[]> v, double probabilty, int width, int height, IRNG random, double stage) {
		var v_tmp = v.duplicate();
		for (int i = 0; i < v_tmp.getData().length; i++) {
			if(i == 0) {
				if(random.nextDouble() < probabilty) {
					v_tmp.getData()[i] = IRandomGASolution.getRadnomByPosition(i, width, height, random);
				}
				continue;
			}
			if(random.nextDouble() < probabilty) {
				v_tmp.getData()[i] = IRandomGASolution.getRadnomByPosition(i, width, height, random);
				v_tmp.getData()[i+1] = IRandomGASolution.getRadnomByPosition(i+1, width, height, random);
				v_tmp.getData()[i+2] = IRandomGASolution.getRadnomByPosition(i+2, width, height, random);
				v_tmp.getData()[i+3] = IRandomGASolution.getRadnomByPosition(i+3, width, height, random);
				v_tmp.getData()[i+4] = IRandomGASolution.getRadnomByPosition(i+4, width, height, random);
			}
			i = i + 4;
		}
		return v_tmp;
		
	}

}
