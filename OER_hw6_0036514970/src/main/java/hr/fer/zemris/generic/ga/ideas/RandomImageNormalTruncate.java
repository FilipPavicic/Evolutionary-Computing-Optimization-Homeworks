package hr.fer.zemris.generic.ga.ideas;

import org.apache.commons.math3.distribution.NormalDistribution;
import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.optjava.rng.IRNG;

public class RandomImageNormalTruncate implements IRandomGASolution {
	
	private static final NormalDistribution DISTRIBUTION = new NormalDistribution(0, 1);

	@Override
	public GAImageSolution get(int Np,int width, int height, IRNG random) {
		int[] data = new int[1 + 5 * Np];
		for (int i = 0; i < data.length; i++) {
			data[i] = getRadnomByPositionTruncateNormal(i,data.length, width, height, random);
		}
		return new GAImageSolution(data);
	}
	
	public static  int getRadnomByPositionTruncateNormal(int i,int size,  int width, int height, IRNG random) {
		if(i == 0) {
			return  random.nextInt(0, 256);
		}
		switch ((i-1) % 5) {
		case 0:  return random.nextInt(0,width);
		case 1: return random.nextInt(0,height);
		case 2: {
			double p = random.nextDouble();
			double vrijednost = randomTruncateNormalForP(p, (1.0 * i /size) * (width-3),3, width-3);
			return width - (int)Math.round(vrijednost);
		}
		case 3: {
			double p = random.nextDouble();
			double vrijednost = randomTruncateNormalForP(p, (1.0 * i /size) * (height-3),3, height-3);
			return height - (int)Math.round(vrijednost);
		}
		case 4: return  random.nextInt(0, 256);
		default:
			throw new IllegalArgumentException("Unexpected value: " + ((i-1) % 5));
		}

	}
	
	public static double randomTruncateNormalForP(double p, double mean, double lb, double ub) {
		if(p < 0 || p > 1) throw new IllegalArgumentException("Vjerovatnost mora biti između 1 i 0 ");
		//harkodirana st devijaca
		double sd = (ub-lb) / 10.0;
		double cdf_a = DISTRIBUTION.cumulativeProbability(((lb - mean)/sd));
		double cdf_b = DISTRIBUTION.cumulativeProbability(((ub - mean)/sd));
		double Z = cdf_b - cdf_a;
		double val = p * Z + cdf_a;
		return mean + sd * DISTRIBUTION.inverseCumulativeProbability(val);
	}

	

}
