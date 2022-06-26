package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Clause {
	
	private int indexes[];
	
	public Clause(int[] indexes) {
		this.indexes = indexes;
	}
	
	// vraæa broj literala koji èine klauzulu
	public int getSize() {
		return indexes.length;
	}
	
	// vraæa indeks varijable koja je index-ti èlan ove klauzule
	public int getLiteral(int index) {
		return indexes[index];
	}
	// vraæa true ako predana dodjela zadovoljava ovu klauzulu
	public boolean isSatisfied(BitVector assignment) {
		return Arrays.stream(indexes)
			.anyMatch((i) ->  i > 0 ? (assignment.get(i - 1) == true) : (assignment.get(-i - 1) == false));
	}
	@Override
	public String toString() {
		return Arrays.stream(indexes)
				.mapToObj(i-> Integer.valueOf(i).toString()).collect(Collectors.joining());
	}
}