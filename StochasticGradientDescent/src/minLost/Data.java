package minLost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVWriter;

public class Data {
	
	public static HashMap<String, Double> read_V(String v_filename) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(v_filename));
		HashMap<String, Double> V = new HashMap<String, Double>();
		String m_n = sc.nextLine();
		while(sc.hasNext()){
			String[] ij_Vij = sc.nextLine().split(" ");
			Double Vij = Double.parseDouble(ij_Vij[1]);
			V.put(ij_Vij[0], Vij);
		}
		sc.close();
		return V;
	}
	
	public static void create_V(String filename, int m, int n, int max_value) throws IOException{
		//create file V
		CSVWriter writer = new CSVWriter(new FileWriter("V.csv"), ';');
		
		System.out.println("[data/create_V]");
		File V_File = new File(filename);
		if(V_File.exists()){
			System.out.println("[data/create_v] V.in exists");
			V_File.delete(); 
		}
		V_File.createNewFile();
		
		//open file V
		FileWriter fw = new FileWriter(V_File);
		fw.write(m + " " + n + "\n");
		writer.writeNext("V : " + m + " " + n);
		
		Random random = new Random();
		
		for(int i = 0 ; i < m ; i ++){
			for(int j = 0 ; j < n ;  j++){
				int value = random.nextInt(max_value + 1);
				fw.write(i + "," + j + " " + value + "\n");
				writer.writeNext("", i + "," + j, ""+value);
			}
		}
		
		fw.close();
		writer.close();
		System.out.println("> file  V.in created");
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
		
//		System.out.println("max_value=" + max_value);
		
		double[][] M = new double[row][col];
		
		Random random = new Random();
		
		for(int i = 0 ; i < row ; i++){
			for(int j = 0 ; j < col ; j++){
				double random_value = random.nextGaussian() * max_value  ; // + random.nextInt(max_value);
//				double value = random_value - (max_value/2);
				if(random_value > max_value){
					random_value = max_value;
				}
				if(random_value < -max_value){
					random_value = -max_value;
				}
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
}
