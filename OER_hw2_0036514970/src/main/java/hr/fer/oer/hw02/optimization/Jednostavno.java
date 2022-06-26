package hr.fer.oer.hw02.optimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;



public class Jednostavno {
	
	public static final Map<String, IFunction> FUNCTIONS = new HashMap<>();
	static {
		FUNCTIONS.put("1", new PolynomialTwoVariableFunction(1,0,1,1));
		FUNCTIONS.put("2", new PolynomialTwoVariableFunction(1,1,10,2));
	}
	
	

	public static void main(String[] args) {
		IFunction f = FUNCTIONS.get(args[0]);
		int iteration  = Integer.parseInt(args[1]);
		double[] start = new Random().doubles(f.numberOfVariable()).toArray();
		for (int i = 2; i < args.length; i++) {
			start[i - 2] = Double.parseDouble(args[i]); 
		}
		System.out.println(
				NumOptAlgorithms.getVectorString(start, "Start")
		);
		NumOptAlgorithms.gradientSlide(f, iteration,start, true, false);



		//PolynomialTwoVariableFunction f = new PolynomialTwoVariableFunction(2,3,3,2);
		//NumOptAlgorithms.gradientSlide(f, 100, new double[] {0,0});
		//LinearSystemFunction l = new LinearSystemFunction(new double[][] {{1,2},{2,3}}, new double[] {5,8});
		//NumOptAlgorithms.gradientSlide(f, 100, new double[] {0,0});
//		Function4Zadatak F4 = new Function4Zadatak(new double[][]  {{2,2,2,2,2,2}});
//		double[] a = F4.gradient(new double[] {1,1,1,1,1,1});
//		System.out.println(F4.gradient(new double[] {1,1,1,1,1,1}));
		//RealMatrix m = readMatrix("./zad4_data.txt");
//		double[][] A = m.getSubMatrix(0, m.getRowDimension()-1, 0, m.getColumnDimension()-2).getData();
//		double[] x = m.getColumn(m.getColumnDimension()-1);
		//Function4Zadatak F4 = new Function4Zadatak(m.getData());
		//NumOptAlgorithms.gradientSlide(F4, 1000, new double[] {-2,2,1,3,-5,1});
//				LinearSystemFunction l = new LinearSystemFunction(A,x);
//				NumOptAlgorithms.gradientSlide(l, 1000, new double[l.numberOfVariable()]);


	}

	public static RealMatrix readMatrix(String path) {
		List<double[]> rows = new ArrayList<>();
		Scanner myReader = null;
		try {
			File myObj = new File(path);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(data.startsWith("#")) continue;
				rows.add(Arrays.stream(data.substring(1,data.length() -1).replace(" ", "").split(","))
						.mapToDouble((e) -> Double.parseDouble(e)).toArray());
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally {
			myReader.close();
		}
		RealMatrix M = new Array2DRowRealMatrix(rows.size(),rows.get(0).length);
		for (int i = 0; i < rows.size(); i++) {
			M.setRow(i, rows.get(i));
		}
		return M;
	}
}

