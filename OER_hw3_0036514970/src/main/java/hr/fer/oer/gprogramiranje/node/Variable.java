package hr.fer.oer.gprogramiranje.node;

import java.util.Random;

public class Variable extends Node {
	
	int index;
	
	public Variable(String name, int index) {
		super(name);
		this.index = index;
	}

	@Override
	public double solve(double[] variable) {
		return variable[index];
	}

	@Override
	public Node copy() {
		return new Variable(name, index);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int deep() {
		return 1;
	}
	
	@Override
	public void setChildren(Node... children) {
		throw new IllegalStateException("Varijabla nema dijecu");
	}
	
	public static Node randomOfVariable(int numberOfVariable, Random random) {
		int var = random.nextInt(numberOfVariable);
		return new Variable("x"+var, var);	
	}
}
