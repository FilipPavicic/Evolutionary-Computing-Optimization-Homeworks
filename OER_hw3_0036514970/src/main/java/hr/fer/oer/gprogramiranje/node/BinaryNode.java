package hr.fer.oer.gprogramiranje.node;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;

public class BinaryNode  extends Node{
	
	
	
	BiFunction<Double, Double, Double> operator;

	public BinaryNode(Node left,Node rigtht, BiFunction<Double, Double, Double> operator, String name) {
		super(Arrays.asList(left, rigtht), name);
		this.operator = operator;
	}
	public BinaryNode(BiFunction<Double, Double, Double> operator, String name) {
		super(name);
		this.operator = operator;
	}
	

	@Override
	public double solve(double[] variable) {
		return operator.apply(children.get(0).solve(variable), children.get(1).solve(variable));
	}
	
	@Override
	public String toString() {
		return "(" + children.get(0) + " "+name+" " + children.get(1) +")"; 
	}

	@Override
	public Node copy() {
		return new BinaryNode(children.get(0).copy(), children.get(1).copy(), operator, name);
	}

}
