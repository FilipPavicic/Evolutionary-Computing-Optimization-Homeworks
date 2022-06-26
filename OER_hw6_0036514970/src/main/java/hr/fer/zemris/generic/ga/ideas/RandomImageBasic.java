package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class RandomImageBasic implements IRandomGASolution {

	@Override
	public GAImageSolution get(int Np,int width, int height, IRNG random) {
		int[] data = new int[1 + 5 * Np];
		for (int i = 0; i < data.length; i++) {
			data[i] = IRandomGASolution.getRadnomByPosition(i, width, height, random);
		}
		return new GAImageSolution(data);
	}
	
	

	

}
