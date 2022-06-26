package hr.fer.oer.galgoritam;

import java.util.Arrays;
import java.util.Random;

public class ArrayDoubleSolution {
	
	private double[] array;

	public ArrayDoubleSolution(double[] array) {
		super();
		this.array = array;
	}
	
	public ArrayDoubleSolution(int n, double min, double max) {
		array = new Random().doubles(n).map((e) -> e * (max - min) + min).toArray();
	}

	@Override
	public String toString() {
		return Arrays.toString(array);
	}

	public double[] getArray() {
		return array;
	}

	public void setArray(double[] array) {
		this.array = array;
	}
	
	
	
	
	
	
}
