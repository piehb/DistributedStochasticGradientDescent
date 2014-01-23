package cost;

import java.io.IOException;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

public class CostFunction {

	public static float LSNZ(Map<String, Double> V, double[][] W, double[][]H , int max_value){
		
		int r = H.length;
		
		float L = 0;
		
		for(String ij : V.keySet()){
			String[] i_j = ij.split(",");
			int i = Integer.parseInt(i_j[0]);
			int j = Integer.parseInt(i_j[1]);
			
			double WH_ij = 0;
			for(int k = 0 ; k < r ; k++){
//				System.out.println("i: " + i + " , j: " + j + " , r: " + r);
				WH_ij += W[i][k] * H[k][j];
			}
			
			L += Math.pow(V.get(ij) - WH_ij + (max_value/2), 2);
		}
		return L;
	}

	public static float LSNZ(double[][] V_, double[][] W, double[][] H, int max_value) throws IOException {
		// TODO Auto-generated method stub
		int m = V_.length, n = V_[0].length, r = H.length;;		
		float L = 0;
		
		for(int i = 0 ; i < m ; i++)
			for(int j = 0 ; j < n ; j++){
				double WH_ij = 0;
				for(int k = 0 ; k < r ; k++){
					WH_ij += W[i][k] * H[k][j];
				}
				L += Math.pow(V_[i][j] - WH_ij + (max_value/2), 2);
			}
		return L;
	}
	
	public static void printWH(double[][] A, double[][] B) throws IOException {
		// TODO Auto-generated method stub
		int m = A.length, n = B[0].length , r = A[0].length;
		
		double[][] C = new double[m][n];
		
		for(int i = 0 ; i < m ; i++)
			for(int j = 0 ; j < n ; j++){
				C[i][j] = 0;
				for(int k = 0 ; k < r ; k++){
					C[i][j] += A[i][k] * B[k][j]; 
				}
				System.out.print(i + "," + j + "="+ C[i][j] + ", ");
			}
	}
	
	public static void printWH(double[][] A, double[][] B, CSVWriter writer_WH) {
		// TODO Auto-generated method stub
		int m = A.length, n = B[0].length , r = A[0].length;
		
		double[][] C = new double[m][n];
		
		for(int i = 0 ; i < m ; i++)
			for(int j = 0 ; j < n ; j++){
				C[i][j] = 0;
				for(int k = 0 ; k < r ; k++){
					C[i][j] += A[i][k] * B[k][j]; 
				}
				writer_WH.writeNext("", i + "," + j, "" + (""+C[i][j]).replace(".", ","));				
			}
	}	
	
	public static double[][] getWH(double[][] A, double[][] B) {
		// TODO Auto-generated method stub
		int m = A.length, n = B[0].length , r = A[0].length;
		
		double[][] C = new double[m][n];
		
		for(int i = 0 ; i < m ; i++)
			for(int j = 0 ; j < n ; j++){
				C[i][j] = 0;
				for(int k = 0 ; k < r ; k++){
					C[i][j] += A[i][k] * B[k][j]; 
				}
				System.out.print(i + "," + j + "="+ C[i][j] + ", ");
			}
		
		return C;
	}
}
