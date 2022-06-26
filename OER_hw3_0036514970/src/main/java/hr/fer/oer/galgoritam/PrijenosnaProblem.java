package hr.fer.oer.galgoritam;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.apache.commons.math3.distribution.*;

import hr.fer.oer.tools.IProblem;
import hr.fer.oer.tools.Tools;


public class PrijenosnaProblem implements IProblem<ArrayDoubleSolution> {
	
	BiFunction<double[], double[], Double> f = (c, x) -> 
	c[0] * x[0] +
	c[1] * Math.pow(x[0],3) * x[1] +
	c[2] * Math.exp(c[3] * x[2]) * (1 + Math.cos(c[4] * x[3])) +
	c[5] * x[3] * Math.pow(x[4],2);
	
	/**
	 * [x1, x2, x3, x4, x5, y]
	 */
	double values[][];
	
	
	private static final Random RANDOM = new Random();
	private static final ArrayList<BiFunction<ArrayDoubleSolution, ArrayDoubleSolution, ArrayDoubleSolution>> CROSS_FUNCTIONS = new ArrayList<>();
	private static final double[] SIGMAS = {0.1,0.9};
	static {
		CROSS_FUNCTIONS.add((a,b) -> {
			double tmp[] =  new double[a.getArray().length];
			for (int i = 0; i < tmp.length; i++) {
				double aI = a.getArray()[i];
				double bI = b.getArray()[i];
				tmp[i] = RANDOM.nextDouble() * Math.abs(aI - bI) + Math.min(aI, bI);
			}
			return new ArrayDoubleSolution(tmp);
		});
		CROSS_FUNCTIONS.add((a,b) -> {
			double tmp[] =  new double[a.getArray().length];
			for (int i = 0; i < tmp.length; i++) {
				double d = RANDOM.nextDouble();
				if(d >= 0.2) {
					tmp[i] = a.getArray()[i];
					continue;
				}
				double aI = a.getArray()[i];
				double bI = b.getArray()[i];
				tmp[i] = RANDOM.nextDouble() * Math.abs(aI - bI) + Math.min(aI, bI);
			}
			return new ArrayDoubleSolution(tmp);
		});
	}
	
	
	public PrijenosnaProblem(double[][] values) {
		super();
		this.values = values;
	}
	
	public PrijenosnaProblem(String path) {
		this.values = Tools.readMatrix(path);
	}

	

	@Override
	public double fit(ArrayDoubleSolution solution) {
		return 1.0 / Stream.of(values).mapToDouble((e) -> Math.pow(f.apply(solution.getArray(), e) - e[e.length-1], 2.0)).sum() * values.length;
	}

	@Override
	public ArrayDoubleSolution cross(ArrayDoubleSolution first, ArrayDoubleSolution second) {
		int index = RANDOM.nextInt(CROSS_FUNCTIONS.size());
		return CROSS_FUNCTIONS.get(index).apply(first, second);
	}

	@Override
	public void mutate(ArrayDoubleSolution cross) {
		for (int i = 0; i < cross.getArray().length; i++) {
			if(RANDOM.nextDouble() <= 0.3)
				cross.getArray()[i] += new  NormalDistribution(0,SIGMAS[RANDOM.nextInt(SIGMAS.length)]).sample(); 
		}
		
	}

}
