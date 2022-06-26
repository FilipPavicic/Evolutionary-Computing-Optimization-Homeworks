package hr.fer.oer.hw02.optimization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Function4Zadatak  implements IFunction {

	BiFunction<double[], double[], Double> f = (c, x) -> 
	c[0] * x[0] +
	c[1] * Math.pow(x[0],3) * x[1] +
	c[2] * Math.exp(c[3] * x[2]) * (1 + Math.cos(c[4] * x[3])) +
	c[5] * x[3] * Math.pow(x[4],2)	+
	-x[5]
			;

	BiFunction<double[], double[], Double> df_da = (coef, x) -> x[0];
	BiFunction<double[], double[], Double> df_db = (coef, x) -> Math.pow(x[0],3) * x[1];
	BiFunction<double[], double[], Double> df_dc = (coef, x) -> Math.exp(coef[3] * x[2]) * (1 + Math.cos(coef[4] * x[3]));
	BiFunction<double[], double[], Double> df_dd = (coef, x) -> coef[2] * x[2] * Math.exp(coef[3] * x[2]) * (1 + Math.cos(coef[4] * x[3]));
	BiFunction<double[], double[], Double> df_de = (coef, x) -> coef[2] * Math.exp(coef[3] * x[2]) * x[3] *  -Math.sin(coef[4] * x[3]);
	BiFunction<double[], double[], Double> df_df = (coef, x) -> x[3] * Math.pow(x[4],2);


	List<BiFunction<double[], double[], Double>> derivations;

	/**
	 * [x1, x2, x3, x4, x5, y]
	 */
	double values[][];


	public Function4Zadatak(double[][] values) {
		super();
		this.values = values;
		derivations = new ArrayList<BiFunction<double[],double[],Double>>();
		derivations.add(df_da);
		derivations.add(df_db);
		derivations.add(df_dc);
		derivations.add(df_dd);
		derivations.add(df_de);
		derivations.add(df_df);

	}

	@Override
	public int numberOfVariable() {
		return 6;
	}

	@Override
	public double solve(double[] point) {
		if(point.length != numberOfVariable()) throw new IllegalArgumentException("Dimenzija zadane točke nije jednaka broju varijabli");
		double rez = 0;
		for (double[] xs : values) {
			rez += Math.pow(f.apply(point,xs), 2);
		}
		return rez / 2;
	}

	@Override
	public double[] gradient(double[] point) {
		if(point.length != numberOfVariable()) throw new IllegalArgumentException("Dimenzija zadane točke nije jednaka broju varijabli");
		RealMatrix G_vector = new Array2DRowRealMatrix(values.length, 1);
		RealMatrix Jacobian = new Array2DRowRealMatrix(values.length, numberOfVariable());
		for (int i = 0; i < values.length; i++) {
			G_vector.setEntry(i, 0, f.apply(point, values[i]));
			for (int j = 0; j < numberOfVariable(); j++) {
				Jacobian.setEntry(i, j, derivations.get(j).apply(point, values[i]));
				//System.out.println("der1: " + derivations.get(j).apply(point, values[i]));
				//System.out.println("der2: " + derivateFunction(f, values[i], point, j));
			}
		}
		double[] gradient = Jacobian.transpose().multiply(G_vector).getColumn(0);
		RealVector v = new ArrayRealVector(gradient);
		if(v.getNorm() > 1) {
			gradient = v.mapDivideToSelf(v.getNorm()).toArray();
		}
		return gradient;
	}

	public static double derivateFunction(
			BiFunction<double[], double[], Double> f, double[] x, double[] c, int variable) 
	{
		double theta = 1e-15;
		double[] c_th = c.clone();
		c_th[variable] += theta;
		return (f.apply(c_th, x) - f.apply(c, x)) / theta;
	}

}
