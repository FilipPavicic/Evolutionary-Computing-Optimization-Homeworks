package hr.fer.oer.gprogramiranje.node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Node {

	protected List<Node> children;
	String name;

	public Node(List<Node> children, String name) {
		super();
		this.name = name;
		this.children = children;
	}

	public Node(String name) {
		super();
		this.name = name;
		this.children = Collections.emptyList();
	}



	public List<Node> getChildren() {
		return children;
	}


	public abstract double solve(double[] variable);

	public int numberOfElements() {
		return 1 + children.stream().mapToInt((n) -> n.numberOfElements()).sum();
	}

	public abstract Node copy();

	public int deep() {
		return 1 + children.stream().mapToInt((e) -> e.deep()).max().getAsInt();
	}

	public void setChildren(Node... children) {
		this.children = Arrays.asList(children);
	}
	
}
