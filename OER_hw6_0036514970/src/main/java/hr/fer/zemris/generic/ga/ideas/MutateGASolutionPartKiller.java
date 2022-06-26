package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class MutateGASolutionPartKiller implements IMutateGaSolution {
	
	private static final MutateGASolutionPart MP = new MutateGASolutionPart();

	@Override
	public GASolution<int[]> mutate(GASolution<int[]> v, double probabilty, int width, int height, IRNG random, double stage) {
		double r = random.nextDouble();
		if(r > stage) {
			return MP.mutate(v, probabilty, width, height, random, stage);
		}
		
		var v_tmp = v.duplicate();
		for (int i = v_tmp.getData().length -10; i >= 0;) {
			int max_index = i + 2;
			int min_index = i + 7;
			
			if(v_tmp.getData()[max_index] * v_tmp.getData()[max_index +1] < v_tmp.getData()[min_index] * v_tmp.getData()[min_index+1]) {
				max_index = i + 7;
				min_index = i + 2;
			}
			
			double max_pov = v_tmp.getData()[max_index] * v_tmp.getData()[max_index +1];
			double min_pov = v_tmp.getData()[min_index] * v_tmp.getData()[min_index +1];
			double prob = (Math.log(max_pov) - Math.log(min_pov)) /(Math.log(max_pov) + Math.log(min_pov));
			


			if(random.nextDouble() < prob) {
				v_tmp.getData()[max_index] = v_tmp.getData()[max_index];
				v_tmp.getData()[max_index +1] = v_tmp.getData()[max_index +1];
				break;
			}
			i = i - 10;
		}
		return v_tmp;
		
	}

}
