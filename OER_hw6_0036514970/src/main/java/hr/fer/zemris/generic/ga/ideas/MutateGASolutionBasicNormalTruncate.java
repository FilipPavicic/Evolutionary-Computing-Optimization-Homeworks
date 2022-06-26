package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class MutateGASolutionBasicNormalTruncate implements IMutateGaSolution {

	@Override
	public GASolution<int[]> mutate(GASolution<int[]> v, double probabilty, int width, int height, IRNG random, double stage) {
		var v_tmp = v.duplicate();
		for (int i = 0; i < v_tmp.getData().length; i++) {
			if(random.nextDouble() < probabilty) {
				v_tmp.getData()[i] = RandomImageNormalTruncate.getRadnomByPositionTruncateNormal(i,v_tmp.getData().length, width, height, random);
			}
		}
		return v_tmp;
		
	}

}
