package cost;

import java.io.IOException;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

public class CostFunction {

	public static Double LSNZ(Map<String, Double> V, double[][] W, double[][]H){
		
		int r = H.length;
		
		double L = 0;
		
		for(String ij : V.keySet()){
			String[] i_j = ij.split(",");
			int i = Integer.parseInt(i_j[0]);
			int j = Integer.parseInt(i_j[1]);
			
			double WH_ij = 0;
			for(int k = 0 ; k < r ; k++){
				WH_ij += W[i][k] * H[k][j];
			}
			
			L += Math.pow(V.get(ij) - WH_ij, 2);
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
