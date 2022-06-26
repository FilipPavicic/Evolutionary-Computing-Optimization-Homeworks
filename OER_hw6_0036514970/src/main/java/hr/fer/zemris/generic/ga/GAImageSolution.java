package hr.fer.zemris.generic.ga;

public class GAImageSolution extends GASolution<int[]>{
	
	public GAImageSolution(int[] data) {
		this.data = data;
	}

	@Override
	public GASolution<int[]> duplicate() {
		GAImageSolution t = new GAImageSolution(data.clone());
		t.fitness = this.fitness;
		return t;
	}

}
