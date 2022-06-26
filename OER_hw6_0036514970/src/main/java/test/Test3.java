package test;

import hr.fer.zemris.generic.ga.ideas.RandomImageNormalTruncate;

public class Test3 {

	public static void main(String[] args) {
		// Test 
		double mean = 150;
		double lb = 0;
		double ub = 200;
		/*
		133.07868743069747
		149.84434520183703
		175.006689071807
		*/
		double p0_2 = RandomImageNormalTruncate.randomTruncateNormalForP(0.2, mean, lb, ub); 
		System.out.println(p0_2);
		double p0_5 = RandomImageNormalTruncate.randomTruncateNormalForP(0.5, mean, lb, ub);
		System.out.println(p0_5);
		double p0_9 = RandomImageNormalTruncate.randomTruncateNormalForP(0.9, mean, lb, ub);
		System.out.println(p0_9);
		
	}

}
