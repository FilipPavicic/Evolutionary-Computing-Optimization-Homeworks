package hr.fer.oer.de.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Tools {
	
	
	public static double[][] readVariables(String path) {
		List<double[]> rows = new ArrayList<>();
		Scanner myReader = null;
		try {
			File myObj = new File(path);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(data.startsWith("#")) continue;
				rows.add(Arrays.stream(data.split("\t"))
						.mapToDouble((e) -> Double.parseDouble(e)).toArray());
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally {
			myReader.close();
		}
		return  rows.toArray(new double[][] {{}});
	}
	public static double[][] readMatrix(String path) {
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
		return  rows.toArray(new double[][] {{}});
	}
	
	public static Properties readProperties(String path) {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(path)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return prop;
	}
}
