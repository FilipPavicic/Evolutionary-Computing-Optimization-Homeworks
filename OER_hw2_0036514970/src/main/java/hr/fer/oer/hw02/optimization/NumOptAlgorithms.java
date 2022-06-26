package hr.fer.oer.hw02.optimization;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class NumOptAlgorithms {
	
	public static final double EPSILON = 1e-4;

	
	public static boolean equalsScalar(double a, double b) {
		boolean e =  Math.abs(a-b) < EPSILON;
		//if(!e) System.out.println("a: " + a +", b: " +b);
		return e;
	}
	public static boolean equalsVector(double[] a, double[] b) {
		if(a.length != b.length) return false;
		for (int i = 0; i < b.length; i++) {
			if(!equalsScalar(a[i], b[i])) return false;
		}
		return true;
	}
	
	public static double[] gradientSlide(IFunction f, int maxIteration, double[] startPoint, boolean printPoint, boolean PrintError) {
		System.out.println("Usao u gradijentni spust");
		double zeros[] = new double[startPoint.length];
		for (int i = 0; i < maxIteration; i++) {
			double lambda = bisection(f,startPoint);
			if(equalsVector(f.gradient(startPoint),zeros)) return startPoint;
			startPoint = sumVector(dot(f.gradient(startPoint),-lambda), startPoint);
			if(printPoint) System.out.println(getVectorString(startPoint, "Rješenje nakon koraka: " + i ));
			//System.out.println("Gradijent" + Arrays.stream(f.gradient(startPoint)).mapToObj(e -> ""+e).collect(Collectors.joining(", ", "[", "]")));
			if(PrintError) System.out.println("Greška: " + f.solve(startPoint) + " korak: " + i);
		}
		return startPoint;
	}
	private static double bisection(IFunction f, double[] startPoint) {
		double lambda = 0;
		int counter = 0;
		double gradient[] = f.gradient(startPoint);
		while(true) {
			if(lambda == Double.POSITIVE_INFINITY) throw new IllegalStateException("Dosegnut max lambda, funkciji nije moguće naći pregib");
			lambda = Math.pow(2, counter);
			double x[] =f.gradient(sumVector(startPoint, dot(gradient,-lambda)));
			if(-vectorProduct(x,gradient) > 0) break;
			counter++;
		}
		double low = 0;
		double mid = lambda / 2;
		double high = lambda;
		double x_mid[];
		
		do {
			x_mid =f.gradient(sumVector(startPoint, dot(gradient,-mid)));
			//System.out.println("Gradijent: " + -vectorProduct(x_mid,gradient));
			//System.out.println("mid: " + mid);
			if(-vectorProduct(x_mid,gradient) < 0) low = mid;
			else high = mid;
			mid = (low + high) / 2;	
			
		} while(!equalsScalar(0, -vectorProduct(x_mid,gradient)));
		
		return mid;		
	}
	
	public static double vectorProduct(double[] a, double[] b) {
		if(a.length != b.length) throw new IllegalArgumentException("Vektori moraju biti istih dimenzija");
		double res = 0;
		for (int i = 0; i < b.length; i++) {
			res += a[i]*b[i];
		}
		return res;
	}
	public static double[] dot(double[] a, double lambda) {
		double rez[] = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			rez[i] = a[i] * lambda;
		}
		return rez;
	}
	
	public static double[] sumVector(double[] a, double[] b) {
		if(a.length != b.length) throw new IllegalArgumentException("Vektori moraju biti istih dimenzija");
		double rez[] = new double[a.length];
		for (int i = 0; i < b.length; i++) {
			rez[i] = a[i] + b[i];
		}
		return rez;
	}
	
	public static String getVectorString(double[] vector, String name) {
		return Arrays.stream(vector)
				.mapToObj((e) -> "" + e)
				.collect(Collectors.joining(", ", name + " = [", "]"));
	}
	 
	
}
