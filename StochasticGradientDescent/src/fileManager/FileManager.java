package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class FileManager {
	public static HashMap<String, Double> read_V(String v_filename) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(v_filename));
		HashMap<String, Double> V = new HashMap<String, Double>();
//		sc.nextLine();		// first line is <row col>
		while(sc.hasNext()){
			String[] ij_Vij = sc.nextLine().split(" ");
			Double Vij = Double.parseDouble(ij_Vij[1]);
			V.put(ij_Vij[0], Vij);
		}
		sc.close();
		return V;
	}
	
	public static double[][] read_Matrix(String filename) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(filename));
		String[] firstLine = sc.nextLine().split(" ");		// first line is <row col>
//		System.out.println("firstLine = " + firstLine[0] + "   " + firstLine[1]);
		int row = Integer.parseInt(firstLine[0]);
		int col = Integer.parseInt(firstLine[1]);
		double[][] out = new double[row][col];
		while(sc.hasNext()){
			
			String[] ij_Vij = sc.nextLine().split(" ");
			
			String[] line = ij_Vij[0].split(",");		// first line is <row col>
			row = Integer.parseInt(line[0]);
			col = Integer.parseInt(line[1]);
			
			Double Vij = Double.parseDouble(ij_Vij[1]);
			
			out[row][col] = Vij;
		}
		sc.close();
		return out;
	}
	
	public static double[][] read_v(String filename, int row, int col) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(filename));
		double[][] out = new double[row][col];
		while(sc.hasNext()){
			
			String[] ij_Vij = sc.nextLine().split(" ");
			
			String[] line = ij_Vij[0].split(",");		// first line is <row col>
			row = Integer.parseInt(line[0]);
			col = Integer.parseInt(line[1]);
			
			Double Vij = Double.parseDouble(ij_Vij[1]);
			
			out[row][col] = Vij;
		}
		sc.close();
		return out;
	}
}
