package hr.fer.zemris.optjava.dz5;

import java.util.Random;


public class BoxConstraint{

	private static final Random RANDOM = new Random();

	private double[] lowerBounds;
	private double[] upperBounds;



	public BoxConstraint(double[] lowerBounds, double[] upperBounds) {
		super();
		if(lowerBounds.length != upperBounds.length) 
			throw new IllegalArgumentException("Gornja i donja ograničenja moraju biti istih dimenzija");
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
	}



	public double[] generateRandomPoint() {
		double[] tmp = new double[lowerBounds.length];
		for (int i = 0; i < tmp.length; i++) {
			double min = lowerBounds[i];
			double max = upperBounds[i];
			tmp[i] = RANDOM.nextDouble() * (max - min) + min;
		}
		return tmp;
	}

	public double randomPositionValue(int position) {
		double min = lowerBounds[position];
		double max = upperBounds[position];
		return  RANDOM.nextDouble() * (max - min) + min;
	}


	public int getDimenzion() {
		return lowerBounds.length;
	}


}
