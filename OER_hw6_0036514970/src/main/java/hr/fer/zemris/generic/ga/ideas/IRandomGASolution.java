package hr.fer.zemris.generic.ga.ideas;

import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.optjava.rng.IRNG;

public interface IRandomGASolution {
	public GAImageSolution get(int Np,int width, int height, IRNG random);
	
	public static  int getRadnomByPosition(int i, int width, int height, IRNG random) {
		if(i == 0) {
			return  random.nextInt(0, 256);
		}
		if(i - 1 % 5 == 4) {
			return random.nextInt(0, 256);
		}
		if(i - 1 % 5 == 0 || i - 1 % 5 == 2) {
			return random.nextInt(0, width);
		}
		return  random.nextInt(0, height);
	}
}
