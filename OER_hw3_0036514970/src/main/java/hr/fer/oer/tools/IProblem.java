package hr.fer.oer.tools;

public interface IProblem<T> {

	public double fit(T solution);

	public T cross(T first, T second);

	public void mutate(T cross);
}
