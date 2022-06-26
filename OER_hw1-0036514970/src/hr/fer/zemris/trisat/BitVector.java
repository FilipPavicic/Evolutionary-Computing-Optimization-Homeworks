package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BitVector {
	 
	protected boolean bits[];
	
	public BitVector(Random rand, int numberOfBits) {
		bits = new boolean[numberOfBits];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = rand.nextBoolean();
		}
	}
	public BitVector(boolean ... bits) {
		this.bits = bits.clone();
	}
	public BitVector(int n) {
		bits = new boolean[n];
		Arrays.fill(bits, false);
	}
	
	// vraæa vrijednost index-te varijable
	public boolean get(int index) {
		return bits[index];
	}
	
	// vraæa broj varijabli koje predstavlja
	public int getSize() {
		return bits.length;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (int i = 0; i < bits.length; i++) {
			output += bits[i] ? "1" : "0";
		}
		return output;
	}
	
	// vraæa promjenjivu kopiju trenutnog rješenja
	public MutableBitVector copy() {
		return new MutableBitVector(bits);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bits);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BitVector))
			return false;
		BitVector other = (BitVector) obj;
		if (!Arrays.equals(bits, other.bits))
			return false;
		return true;
	}
	
	
}