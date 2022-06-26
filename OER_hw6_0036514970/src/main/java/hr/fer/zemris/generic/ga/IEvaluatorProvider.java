package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;

public interface IEvaluatorProvider<T> {
	public IGAEvaluator<T> getEvaluator(GrayScaleImage template);
}
