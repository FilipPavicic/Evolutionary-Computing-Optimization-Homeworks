package hr.fer.zemris.optjava.rng;
/**
 * Suèelje koje predstavlja objekte koji sadrže generator sluèajnih
 * brojeva i koji ga stavljaju drugima na raspolaganje uporabom
 * metode {@link #getRNG()}. Objekti koji implementiraju ovo suèelje
 * ne smiju na svaki poziv metode {@link #getRNG()} stvarati i vraæati
 * novi generator veæ moraju imati ili svoj vlastiti generator koji vraæaju,
 * ili pristup do kolekcije postojeæih generatora iz koje dohvaæaju i vraæaju
 * jedan takav generator (u skladu s pravilima konkretne implementacije ovog
 * suèelja) ili isti stvaraju na zahtjev i potom èuvaju u cache-u za istog
 * pozivatelja.
 *
 * @author marcupic
 */
public interface IRNGProvider {
	/**
	 * Metoda za dohvat generatora sluèajnih brojeva koji pripada
	 * ovom objektu.
	 *
	 * @return generator sluèajnih brojeva
	 */
	public IRNG getRNG();
}