package hr.fer.oer.hw02.optimization;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FunctionTest {
	
	
	
	@Test
	public void testSolutionNoVarialbes() {
		double d[][] = {{1}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {123,123};
		assertTrue(NumOptAlgorithms.equalsScalar(1, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {0, 0}, f.gradient(p)));	
	}
	
	@Test
	public void testSolutionOneVarialbesLinear() {
		double d[][] = {{1,5}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {2,6};
		assertTrue(NumOptAlgorithms.equalsScalar(11, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {5, 0}, f.gradient(p)));	
	}
	
	@Test
	public void testSolutionOneVarialbesPolynomial() {
		double d[][] = {{1,5,3}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {2,6};
		assertTrue(NumOptAlgorithms.equalsScalar(23, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {17, 0}, f.gradient(p)));	
	}
	
	@Test
	public void testSolutionTwoVarialbesLinear() {
		double d[][] = {{1,5},{2,0}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {2,6};
		assertTrue(NumOptAlgorithms.equalsScalar(23, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {5, 2}, f.gradient(p)));	
	}
	
	@Test
	public void testSolutionTwoVarialbesLinear2() {
		double d[][] = {{1,5},{2,1}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {2,6};
		assertTrue(NumOptAlgorithms.equalsScalar(35, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {11, 4}, f.gradient(p)));	
	}
	
	@Test
	public void testSolutionTwoVarialbesPolynomial() {
		double d[][] = {{1,5,1},{2,1,1},{1,2,3}};
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(d);
		double p[] = {2,6};
		assertTrue(NumOptAlgorithms.equalsScalar(675, f.solve(p)));
		assertTrue(NumOptAlgorithms.equalsVector(new double[] {543, 212}, f.gradient(p)));	
	}
	
	@Test
	public void testConstrucor() {
		PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(2,1,2,3);
		double d[][] = {
			{20,-4,2},
			{-12,0,0},
			{2,0,0}
		};
		for (int i = 0; i < d.length; i++) {
			assertTrue(NumOptAlgorithms.equalsVector(f.variables[i], d[i]));
		}
	}
		
	


}
