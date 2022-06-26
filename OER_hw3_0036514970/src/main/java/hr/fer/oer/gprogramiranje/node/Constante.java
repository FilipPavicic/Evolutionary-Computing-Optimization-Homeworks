package hr.fer.oer.gprogramiranje.node;

import java.util.Random;

import hr.fer.oer.tools.Tuple;

public class Constante extends Node {
	
	double constante;
	
	

	public Constante(double constante) {
		super(String.valueOf(constante));
		this.constante = constante;
	}

	@Override
	public double solve(double[] variable) {
		return constante;
	}

	@Override
	public int numberOfElements() {
		return 1;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public Node copy() {
		return new Constante(constante);
	}
	
	@Override
	public int deep() {
		return 1;
	}
	
	
	@Override
	public void setChildren(Node... children) {
		throw new IllegalStateException("Konstanta nema dijecu");
	}
	
	public static Node randomConstnate(Tuple<Double, Double> tuple, Random random) {
		double cons = random.nextDouble() * (tuple.getSecond() - tuple.getFirst()) + tuple.getFirst();
		return new Constante(cons);
	}
}
