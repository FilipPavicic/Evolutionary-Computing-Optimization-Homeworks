package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {
	
	BitVector assignment;
	
	public BitVectorNGenerator(BitVector assignment) {
		this.assignment = assignment;
	}
	// Vraæa lijeni iterator koji na svaki next() raèuna sljedeæeg susjeda
	
	@Override
	public Iterator<MutableBitVector> iterator() {
		return new InternalIterator();
		
	}
	
	// Vraæa kompletno susjedstvo kao jedno polje
	public MutableBitVector[] createNeighborhood() {
		List<MutableBitVector> lista = new ArrayList<MutableBitVector>();
		for (MutableBitVector mutableBitVector : this) {
			lista.add(mutableBitVector);
		}
		return lista.toArray(new MutableBitVector[1]);
	}
	
	
	private class InternalIterator implements Iterator<MutableBitVector> {
		
		int position;
		
		public InternalIterator() {
			position = 0;
		}
		
		@Override
		public boolean hasNext() {
			return position < assignment.getSize();
		}

		@Override
		public MutableBitVector next() {
			MutableBitVector mVector = assignment.copy();
			mVector.set(position, !mVector.get(position));
			position++;
			return mVector;
		}
	}
}