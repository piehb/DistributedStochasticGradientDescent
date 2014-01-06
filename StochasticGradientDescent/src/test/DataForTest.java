package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVWriter;
import cost.CostFunction;

public class DataForTest {
	
	public static void write_matrix(String filename, double[][] M) throws IOException{
		
		//delete file if exists
		File V_File = new File(filename);
		if(V_File.exists()){
			V_File.delete(); 
		}
		V_File.createNewFile();
		
		FileWriter fw = new FileWriter(V_File);
		
		int row = M.length, col = M[0].length;
		
		//write first line <row, col>
		fw.write(row + " " + col + "\n");
		
		for(int i = 0 ; i < row ; i ++){
			for(int j = 0 ; j < col ;  j++){
				fw.write(i + "," + j + " " + M[i][j] + "\n");
			}
		}
		
		fw.close();
	}
	
	public static void create_V_with_missing_values(String filename, String out, ArrayList<String> missing_values) throws IOException{
		//create file V
		File V_File = new File(filename), V_out = new File(out);
		if(V_out.exists()){
			V_out.delete(); 
		}
		V_out.createNewFile();
		
		//open file V
		Scanner sc = new Scanner(V_File);
		FileWriter fw = new FileWriter(V_out);
		
		String m_n = sc.nextLine();
		fw.write(m_n + "\n");
		
		while(sc.hasNext()){
			String ij_Vij_ = sc.nextLine();
//			System.out.println("[data/create_missing] " + ij_Vij_);
			String[] ij_Vij = ij_Vij_.split(" ");
			
			if(!missing_values.contains(ij_Vij[0])){
				fw.write(ij_Vij_ + "\n");
			}	
		}
		sc.close();
		fw.close();
	}
	
	public static ArrayList<String> create_missing_values(int nbr_missing_values, int row, int col){
		
//		System.out.println("[data]create " + nbr_missing_values + " missing values in " + (row*col));
		
		ArrayList<String> missing_values = new ArrayList<String>();
		
		Random random = new Random();
		
		int nbr_missing_added = 0;
		
		while(nbr_missing_added < nbr_missing_values){
			
			int i = random.nextInt(row);
			int j = random.nextInt(col);	
			String ij = "" + i + "," + j;
			
			if(!missing_values.contains(ij)){
				missing_values.add(ij);
				nbr_missing_added++;
			}
		}
		return missing_values;
	}
	
	public static double[][] create_random_matrix(int row, int col, double max_value) throws IOException{
		
		
		double[][] M = new double[row][col];
		
		Random random = new Random();
		
		for(int i = 0 ; i < row ; i++){
			for(int j = 0 ; j < col ; j++){
				double random_value = (double) random.nextInt() / Integer.MAX_VALUE * 2 * max_value;
				random_value = (double) random_value - max_value;
//				System.out.println("random value : " + random_value);
				
				M[i][j] = random_value;
			}
		}
		return M;
	}
	
	public static void create_V_test(String filename, double[][] WH, int m, int n) throws IOException{
		//create file V
		CSVWriter writer = new CSVWriter(new FileWriter("V_test.csv"), ';');
		
		System.out.println("[data/create_V_test]");
		File V_File = new File(filename);
		if(V_File.exists()){
			System.out.println("[data/create_v_test] V_test.in exists");
			V_File.delete(); 
		}
		V_File.createNewFile();
		
		//open file V
		FileWriter fw = new FileWriter(V_File);
		fw.write(m + " " + n + "\n");
		writer.writeNext("V_test : " + m + " " + n);
		
		for(int i = 0 ; i < m ; i ++){
			for(int j = 0 ; j < n ;  j++){
				fw.write(i + "," + j + " " + WH[i][j] + "\n");
				writer.writeNext("" , i + "," + j, "" + WH[i][j]);
			}
		}
		
		fw.close();
		writer.close();
		System.out.println("> file  V_test.in created");
	}
	
	public static void save_error(String output_filename, double[][] V, double[][] W, double[][] H) throws IOException{

		CSVWriter writer_WH = new CSVWriter(new FileWriter(output_filename), ';');
		
		writer_WH.writeNext("WH_GD");
		CostFunction.printWH(W, H, writer_WH);
		writer_WH.close();
		
	}

	/**
	 * 
	 * @param A
	 * @param B
	 * @return A * B
	 */
	public static double[][] matrixMultiplication(double[][] A, double[][] B) {
		int m = A.length, n = B[0].length, r= A[0].length;
		double[][] C = new double[m][n];
		
		double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		
		for(int i = 0 ; i < m ; i++){
			for(int j = 0 ; j < n ; j++){
				C[i][j] = 0;
				for(int k = 0 ; k < r ; k++){
					C[i][j] += (double) A[i][k] * B[k][j];
				}
				if(min > C[i][j])			min = C[i][j];
				else if(max < C[i][j])		max = C[i][j];
			}
		}
//		System.out.println("[ DataForTest/matrixMultiplication] (min, max) = ( " + min + " , " + max + " )");
		return C;
	}
}
