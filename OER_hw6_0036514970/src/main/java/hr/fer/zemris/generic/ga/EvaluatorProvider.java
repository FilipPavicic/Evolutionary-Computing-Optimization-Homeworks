package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class EvaluatorProvider implements IEvaluatorProvider<int[]> {
	GrayScaleImage template;
		
	private  ThreadLocal<IGAEvaluator<int[]>> threadLocal = new ThreadLocal<>() {
		@Override protected IGAEvaluator<int[]> initialValue() {
            return new Evaluator(template);
	}};
	
	private EvaluatorProvider() {}
	 
	@Override
	public  IGAEvaluator<int[]> getEvaluator(GrayScaleImage template) {
		IGAEvaluator<int[]> evaluator = threadLocal.get();
		if(evaluator == null) {
			evaluator = new Evaluator(template);
			threadLocal.set(evaluator);
		}
		return evaluator;
	}
	

}

