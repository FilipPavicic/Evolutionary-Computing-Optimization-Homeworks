package hr.fer.oer.hw02.optimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Sustav {

	public static void main(String[] args) {
		int iteration  = Integer.parseInt(args[0]);
		RealMatrix m = readMatrix(args[1]);
		double[][] A = m.getSubMatrix(0, m.getRowDimension()-1, 0, m.getColumnDimension()-2).getData();
		double[] x = m.getColumn(m.getColumnDimension()-1);
		LinearSystemFunction l = new LinearSystemFunction(A,x);
		NumOptAlgorithms.gradientSlide(l, iteration, new double[l.numberOfVariable()], false, true);

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
