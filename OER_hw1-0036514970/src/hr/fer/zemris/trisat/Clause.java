package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Clause {
	
	private int indexes[];
	
	public Clause(int[] indexes) {
		this.indexes = indexes;
	}
	
	// vra�a broj literala koji �ine klauzulu
	public int getSize() {
		return indexes.length;
	}
	
	// vra�a indeks varijable koja je index-ti �lan ove klauzule
	public int getLiteral(int index) {
		return indexes[index];
	}
	// vra�a true ako predana dodjela zadovoljava ovu klauzulu
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