package hr.fer.oer.gprogramiranje;

import hr.fer.oer.gprogramiranje.node.Node;
import hr.fer.oer.tools.Tuple;

public class Tree {

	Node root;
	double a = 0;
	double b = 1;

	public Tree(Node root) {
		super();
		this.root = root;
	}
	
	public Integer getDeepOfNodeIndex(int i) {
		if(i >= size()) throw new IndexOutOfBoundsException("zadani argument je veći od broja elemenata u stablu");
		return getRecursiveDeepOfNodeIndex(root,i,1).getFirst();
	}

	private Tuple<Integer, Integer> getRecursiveDeepOfNodeIndex(Node node, int i, int deep) {
		if(i == 0) return new Tuple<Integer, Integer>(deep, i);
		for (Node el : node.getChildren()) {
			i--;
			var tuple = getRecursiveDeepOfNodeIndex(el, i,deep + 1);
			Integer a = tuple.getFirst();
			i = tuple.getSecond();
			if(a != null) return new Tuple<Integer,Integer>(a,i);
		}
		return new Tuple<Integer, Integer>(null, i);
	}
	

	public Node getNode(int i) {
		if(i >= size()) throw new IndexOutOfBoundsException("zadani argument je veći od broja elemenata u stablu");
		return getRecursiveNode(root,i).getFirst();
	}



	private Tuple<Node,Integer> getRecursiveNode(Node node, int i) {
		if(i == 0) return new Tuple<Node, Integer>(node, i);
		for (Node el : node.getChildren()) {
			i--;
			var tuple = getRecursiveNode(el, i);
			Node a = tuple.getFirst();
			i = tuple.getSecond();
			if(a != null) return new Tuple<Node,Integer>(a,i);
		}
		return new Tuple<Node, Integer>(null, i);
	}

	public Tree setNode(int i, Node insertNode) {
		if(i >= size()) throw new IndexOutOfBoundsException("zadani argument je veći od broja elemenata u stablu");
		return new Tree(setRecursiveNode(root.copy(),i, insertNode).getFirst());
	}




	private static Tuple<Node,Integer> setRecursiveNode(Node node, Integer i, Node insertNode) {
		if(i < 0) return new Tuple<Node, Integer>(node,i);
		if(i == 0) return new Tuple<Node, Integer>(insertNode,i);
		for (int j = 0; j < node.getChildren().size(); j++) {
			i--;
			if(i < 0) return new Tuple<Node, Integer>(node,i);
			Tuple<Node, Integer> tuple = setRecursiveNode(node.getChildren().get(j), i, insertNode);
			node.getChildren().set(j, tuple.getFirst());
			i = tuple.getSecond();
		}
		return new Tuple<Node, Integer>(node,i);

	}

	public int size() {
		return root.numberOfElements();
	}
	
	public int deep() {
		return root.deep();
	}
	
	@Override
	public String toString() {
		if(a == 1 && b == 0)
			return root.toString();
		return b +" * " + root.toString() + " + " +a;
	}
	
	public Tree copy() {
		return new Tree(root.copy());
	}

	
	public double solve(double[] variable) {
		return b * root.solve(variable) + a;
	}
	
	public static Tree TreeOfNode(Node node) {
		return new Tree(node);
	}
	
	public void setLinearScalling(double b, double a) {
		this.a = a;
		this.b = b;
	}




}
