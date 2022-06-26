package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class MutateGASolutionPartNormalTruncate implements IMutateGaSolution {

	@Override
	public GASolution<int[]> mutate(GASolution<int[]> v, double probabilty, int width, int height, IRNG random, double stage) {
		var v_tmp = v.duplicate();
		for (int i = 0; i < v_tmp.getData().length; i++) {
			if(i == 0) {
				if(random.nextDouble() < probabilty) {
					v_tmp.getData()[i] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i,v_tmp.getData().length, width, height, random);
				}
				continue;
			}
			if(random.nextDouble() < probabilty) {
				v_tmp.getData()[i] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i,v_tmp.getData().length, width, height, random);
				v_tmp.getData()[i+1] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i+1,v_tmp.getData().length, width, height, random);
				v_tmp.getData()[i+2] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i+2,v_tmp.getData().length, width, height, random);
				v_tmp.getData()[i+3] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i+3,v_tmp.getData().length, width, height, random);
				v_tmp.getData()[i+4] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i+4,v_tmp.getData().length, width, height, random);
			}
			i = i + 4;
		}
		return v_tmp;
		
	}

}
