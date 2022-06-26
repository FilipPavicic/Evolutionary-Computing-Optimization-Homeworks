package hr.fer.oer.tools;

public class Tuple<T,K> {
	T first;
	K second;
	
	public Tuple(T first, K second) {
		super();
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public K getSecond() {
		return second;
	}

	public void setSecond(K second) {
		this.second = second;
	}
	
	
	
	
}
