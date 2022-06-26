package hr.fer.zemris.generic.ga;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.tools.Tool;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;
import hr.oer.tools.Tools;

public class Main1 {

	public static void main(String[] args) {
		
		
		Properties p = Tools.readProperties("GAConfiguration.properties");
		GrayScaleImage template = null;
		
		try {
			template = GrayScaleImage.load(new File("images/template.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GA ga = new GA(template, (t) -> new Evaluator(t), p, 1,null);
		long start = System.currentTimeMillis();
		GASolution<int[]> best = ga.doOptimization();
		long end = System.currentTimeMillis();
		System.out.println("Program je završio nakon: " + (end - start) / 1000  + " sekundi");
		GrayScaleImage image = new Evaluator(template).draw(best, null);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
		try {
			image.save(new File("images/image_"+best.fitness+"__"+sdf1.format(timestamp) +".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}


}
