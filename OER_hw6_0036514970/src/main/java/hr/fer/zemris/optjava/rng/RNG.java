package hr.fer.zemris.optjava.rng;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
public class RNG {
	private static IRNGProvider rngProvider;
	static {
		// Stvorite primjerak razreda Properties;
		// Nad Classloaderom razreda RNG tražite InputStream prema resursu rng-config.properties
		// recite stvorenom objektu razreda Properties da se uèita podatcima iz tog streama.
		// Dohvatite ime razreda pridruženo kljuèu "rng-provider"; zatražite Classloader razreda
		// RNG da uèita razred takvog imena i nad dobivenim razredom pozovite metodu newInstance()
		// kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i zapamtite.
		Properties properties = new Properties();
		InputStream is = RNG.class.getClassLoader().getResourceAsStream("rng-config.properties");
		try {
			properties.load(is);
			rngProvider = (IRNGProvider) RNG.class.
					getClassLoader().
					loadClass(properties.getProperty("rng-provider")).
					getDeclaredConstructor().newInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static IRNG getRNG() {
		return rngProvider.getRNG();
	}
}
