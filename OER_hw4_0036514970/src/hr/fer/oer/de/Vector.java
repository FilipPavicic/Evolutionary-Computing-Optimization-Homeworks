package hr.fer.oer.de;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Vector {
	
	private double [] elems;
	
	public Vector(double... elems) {
		this.elems = elems;
	}
	
	public Vector(int n) {
		this.elems = new double[n];
	}
	
	public Vector(int n, Random random, double min, double max) {
		this.elems = random.doubles(n).map(d -> d * (max - min) + min).toArray();
	}
	
	public Vector clone(Vector vector) {
		return new Vector(this.elems.clone());
	}
	
	public int size() {
		return elems.length;
	}
	
	public double[] getArray() {
		return this.elems;
	}
	public void setElement(int i, double value) {
		elems[i] = value;
	}
	
	public double getElement(int i) {
		return elems[i];
	}
	
	public Vector unaryOperator(Function<Double, Double> function) {
		for (double e : elems) {
			e = function.apply(e);
		}
		return this;
	}
	
	public Vector binaryOperator(Vector b,BiFunction<Double ,Double, Double> function) {
		if(this.size() != b.size()) 
			throw new IllegalStateException("Vektori moraju biti iste velièine");
		
		double[] elems_tmp = new double[this.size()];
		for (int i = 0; i < elems_tmp.length; i++) {
			elems_tmp[i] = function.apply(this.elems[i], b.elems[i]);
		}
		return new Vector(elems_tmp);
	}
	
	public Vector dot(double scalar) {
		return unaryOperator((d) -> scalar * d);
	}
	
	public Vector dot(Vector b) {
		return binaryOperator(b, (d1,d2) -> d1 * d2); 
	}
	

	public Vector add(Vector b) {
		return binaryOperator(b, (d1,d2) -> d1 + d2);
	}
	
	/**
	 * Oduzimanje vektora ovim redom this - b
	 * 
	 * @param b
	 * @return
	 */
	public Vector sub(Vector b) {
		return binaryOperator(b, (d1,d2) -> d1 - d2);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(elems);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elems);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (!Arrays.equals(elems, other.elems))
			return false;
		return true;
	}
	
	
	
	
}

