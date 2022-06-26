package hr.fer.oer.gprogramiranje.node;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleFunction;

public class UnaryNode  extends Node{
	
	DoubleFunction<Double> operator;
	

	public UnaryNode(Node child, DoubleFunction<Double> operator, String name) {
		super(Arrays.asList(child), name);
		this.operator = operator;
	}
	
	public UnaryNode(DoubleFunction<Double> operator, String name) {
		super(name);
		this.operator = operator;
	}

	@Override
	public double solve(double[] variable) {
		return operator.apply(children.get(0).solve(variable));
	}
	
	@Override
	public String toString() {
		return name + "(" + children.get(0) + ")";
	}

	@Override
	public Node copy() {
		return new UnaryNode(children.get(0).copy(), operator, name);
	}
	
	@Override
	public void setChildren(Node... children) {
		if(children.length > 1) throw new IllegalStateException("Unarni operator ima sam o ejdno dijete");
		super.setChildren(children);
	}
	
	

}
