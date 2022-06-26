package hr.fer.oer.hw02.optimization;

import org.apache.commons.math3.linear.RealMatrix;

public class Prijenosna {

	public static void main(String[] args) {
		int iteration  = Integer.parseInt(args[0]);
		RealMatrix m = Sustav.readMatrix(args[1]);
		Function4Zadatak F4 = new Function4Zadatak(m.getData());
		NumOptAlgorithms.gradientSlide(F4, iteration, new double[] {1,1}, true, true);
	}
	


}
