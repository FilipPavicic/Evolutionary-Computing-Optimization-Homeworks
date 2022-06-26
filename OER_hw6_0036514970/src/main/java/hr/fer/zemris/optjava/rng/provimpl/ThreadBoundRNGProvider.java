package hr.fer.zemris.optjava.rng.provimpl;

import java.lang.reflect.InvocationTargetException;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

public class ThreadBoundRNGProvider  implements IRNGProvider{

	@Override
	public IRNG getRNG() {
		IRNGProvider provider = null;
		try {
			provider = (IRNGProvider)Thread.currentThread().
					getClass().
					getDeclaredConstructor().
					newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return provider.getRNG();
	}

}
