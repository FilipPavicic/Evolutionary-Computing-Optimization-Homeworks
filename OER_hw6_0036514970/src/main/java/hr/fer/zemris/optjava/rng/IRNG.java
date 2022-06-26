package hr.fer.zemris.optjava.rng;
/**
 * Suèelje koje predstavlja generator sluèajnih brojeva.
 *
 * @author marcupic
 */
public interface IRNG {
	/**
	 * Vraæa decimalni broj iz intervala [0,1) prema uniformnoj distribuciji.
	 *
	 * @return sluèajno generirani decimalni broj
	 */
	public double nextDouble();
	/**
	 * Vraæa decimalni broj iz intervala [min,max) prema uniformnoj distribuciji.
	 *
	 * @param min donja granica intervala (ukljuèiva)
	 * @param max gornja granica intervala (iskljuèiva)
	 *
	 * @return sluèajno generirani decimalni broj
	 */
	public double nextDouble(double min, double max);
	/**
	 * Vraæa decimalni broj iz intervala [0,1) prema uniformnoj distribuciji.
	 *
	 * @return sluèajno generirani decimalni broj
	 */
	public float nextFloat();
	/**
	 * Vraæa decimalni broj iz intervala [min,max) prema uniformnoj distribuciji.
	 *
	 * @param min donja granica intervala (ukljuèiva)
	 * @param max gornja granica intervala (iskljuèiva)
	 *
	 * @return sluèajno generirani decimalni broj
	 */
	public float nextFloat(float min, float max);
	/**
	 * Vraæa cijeli broj iz intervala svih moguæih cijelih brojeva prema uniformnoj distribuciji.
	 *
	 * @return sluèajno generirani cijeli broj
	 */
	public int nextInt();
	/**
	 * Vraæa cijeli broj iz intervala [min,max) prema uniformnoj distribuciji.
	 *
	 * @param min donja granica intervala (ukljuèiva)
	 * @param max gornja granica intervala (iskljuèiva)
	 *
	 * @return sluèajno generirani cijeli broj
	 */
	public int nextInt(int min, int max);
	/**
	 * Vraæa sluèajno generiranu boolean vrijednost. Vrijednosti se izvlaèe
	 * iz uniformne distribucije.
	 *
	 * @return sluèajno generirani boolean
	 */
	public boolean nextBoolean();
	/**
	 * Vraæa decimalni broj iz normalne distribucije s parametrima (0,1).
	 *
	 * @return sluèajno generirani decimalni broj
	 */
	public double nextGaussian();
}