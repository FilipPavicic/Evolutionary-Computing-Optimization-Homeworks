package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class ThreadLocalRNGProvider implements IRNGProvider{
	
	private ThreadLocal<IRNG> threadLocal = new ThreadLocal<>();

	@Override
	public IRNG getRNG() {
		IRNG irng = threadLocal.get();
		if(irng == null) {
			irng = new RNGRandomImpl();
			threadLocal.set(irng);
		}
		return irng;
	}

}
