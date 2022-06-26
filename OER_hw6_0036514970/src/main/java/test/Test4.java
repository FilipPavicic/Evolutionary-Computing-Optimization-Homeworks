package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.generic.ga.GAImageSolution;
import hr.fer.zemris.generic.ga.ideas.RandomImageNormalTruncate;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class Test4 {

	public static void main(String[] args) {
		RandomImageNormalTruncate r = new RandomImageNormalTruncate();
		GAImageSolution image = r.get(200, 200, 133, new RNGRandomImpl());
//		for (int i = 0; i < image.getData().length; i++) {
//			if((i-1) % 5 == 0) System.out.println();
//			System.out.println("iteration: " + i + ", imagedata: " + image.getData()[i]);
//		}
		List<Integer> lista = new ArrayList<Integer>();
		for (int i = 0; i < image.getData().length; i++) {
			if((i-1) % 5 == 0) lista.add(image.getData()[i]);
		}
		System.out.println(lista.stream().map((e) -> e.toString()).collect(Collectors.joining(", ", "[", "]")));
	}

}
