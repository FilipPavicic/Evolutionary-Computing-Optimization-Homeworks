package hr.fer.oer.hw02.optimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class LinearSystemFunction  implements IFunction{
	
	RealMatrix A;
	RealMatrix b;
	
	/**
	 * Ax = b
	 * @param A
	 * @param b
	 */
	public LinearSystemFunction(double[][] A, double[] b) {
		this.A = new Array2DRowRealMatrix(A);
		this.b = new Array2DRowRealMatrix(b);
		
		
	}

	@Override
	public int numberOfVariable() {
		return A.getColumnDimension();
	}

	@Override
	public double solve(double[] point) {
		RealMatrix x = new Array2DRowRealMatrix(point);
		return Math.pow(A.multiply(x).subtract(b).getNorm(), 2);
	}

	@Override
	public double[] gradient(double[] point) {
		RealMatrix x = new Array2DRowRealMatrix(point);
		RealMatrix tmp = A.multiply(x).subtract(b);
		return NumOptAlgorithms.dot(A.transpose().multiply(tmp).getColumn(0),2);
	}
	
	public double meanSquarError(double[] a, double[] b) {
		if(a.length != b.length) throw new IllegalArgumentException("Dimenzije polja moraju biti identične");
		double rez = 0 ;
		for (int i = 0; i < b.length; i++) {
			rez = Math.pow(a[i] - b[i], 2);
		}
		return rez / a.length;
	}

}
