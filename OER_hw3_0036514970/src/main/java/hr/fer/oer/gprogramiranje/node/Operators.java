package hr.fer.oer.gprogramiranje.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Operators {
	public static Node getOperator(String name, Node... children) {
		switch (name) {
		case "+" : return new BinaryNode(children[0],children[1], (a, b) -> a+b,name);
		case "-" : return new BinaryNode(children[0],children[1], (a, b) -> a-b,name);
		case "*" : return new BinaryNode(children[0],children[1], (a, b) -> a*b,name);
		case "/" : return new BinaryNode(children[0],children[1], (a, b) -> b != 0 ? a/b : 1,name);
		case "sin" : return new UnaryNode(children[0], (a) -> Math.sin(a),name);
		case "cos" : return new UnaryNode(children[0], (a) -> Math.cos(a),name);
		case "sqrt" : return new UnaryNode(children[0], (a) -> a >= 0 ? Math.sqrt(a) : 1,name);
		case "log" : return new UnaryNode(children[0], (a) -> a >= 0 ? Math.log(a) : 1,name);
		case "exp" : return new UnaryNode(children[0], (a) -> Math.exp(a),name);
		default : throw new IllegalArgumentException("Unexpected value: " + name);
		}
	}
	
	public static Node getOperator(String name) {
		switch (name) {
		case "+" : return new BinaryNode((a, b) -> a+b,name);
		case "-" : return new BinaryNode((a, b) -> a-b,name);
		case "*" : return new BinaryNode((a, b) -> a*b,name);
		case "/" : return new BinaryNode((a, b) -> b != 0 ? a/b : 1,name);
		case "sin" : return new UnaryNode((a) -> Math.sin(a),name);
		case "cos" : return new UnaryNode((a) -> Math.cos(a),name);
		case "sqrt" : return new UnaryNode((a) -> a >= 0 ? Math.sqrt(a) : 1,name);
		case "log" : return new UnaryNode((a) -> a >= 0 ? Math.log10(a) : 1,name);
		case "exp" : return new UnaryNode((a) -> Math.exp(a),name);
		default : throw new IllegalArgumentException("Unexpected value: " + name);
		}
	}
	
	public static Node getRandom(List<String> operators, Random random) {
		int operator = random.nextInt(operators.size());
		return Operators.getOperator(operators.get(operator));
		
	}
	
	
}
