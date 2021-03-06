package hr.fer.zemris.optjava.rng.rngimpl;

import java.util.Random;

import hr.fer.zemris.optjava.rng.IRNG;

public class RNGRandomImpl implements IRNG{
	
	Random random;
	
	public RNGRandomImpl() {
		this.random = new Random();
	}

	@Override
	public double nextDouble() {
		return random.nextDouble();
	}

	@Override
	public double nextDouble(double min, double max) {
		return this.nextDouble() * (max - min) + min;
	}

	@Override
	public float nextFloat() {
		return random.nextFloat();
	}

	@Override
	public float nextFloat(float min, float max) {
		return this.nextFloat() * (max - min) + min;
	}

	@Override
	public int nextInt() {
		return random.nextInt();
	}

	@Override
	public int nextInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}

	@Override
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	@Override
	public double nextGaussian() {
		return random.nextGaussian();
	}
	

}
