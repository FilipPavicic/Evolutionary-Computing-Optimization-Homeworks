package test;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
public class Test1 {
	public static void main(String[] args) {
		IRNG rng = RNG.getRNG();
		Map<Integer, Integer> mapa = new HashMap<Integer, Integer>();
		for(int i = 0; i < 1000; i++) {
			Integer ir = rng.nextInt(-5, 5);
			if(!mapa.containsKey(ir)) {
				mapa.put(ir, 0);
				continue;
			}
			mapa.put(ir, mapa.get(ir) + 1);
		}
		System.out.println(mapa.toString());
	}
}