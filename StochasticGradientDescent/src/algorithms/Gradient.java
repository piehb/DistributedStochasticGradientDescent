package algorithms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import cost.CostFunction;
import fileManager.CSVManager;
import fileManager.FileManager;

public abstract class Gradient {

	String filename;
	public int m, n, r;
	Map<String, Double> V;
	double[][] W, H;
	double epsilon = 0.0005, convergence;
	long max_iter;
	
	
	public Gradient(String v_filename, double[][] w2, double[][] h2, int m, int n, int r, double convergence, long max_iter) throws FileNotFoundException{
		this.m = m;
		this.n = n;
		this.r = r;
		this.max_iter = max_iter;
		this.convergence = convergence;
		
		V = FileManager.read_V(v_filename);
		W = new double[m][r];
		H = new double[r][n];
		
		//copy W
		for(int i = 0 ; i < m ; i ++){
			for(int k = 0 ; k < r ; k++){
				W[i][k] = w2[i][k];
			}
		}
		
		//copy H
		for(int k = 0 ; k < r ; k++){
			for(int j = 0 ; j < n ; j++){
				H[k][j] = h2[k][j];
			}
		}
	}
	
	public double run(String childname, String csv_file) throws IOException{
		double L;
		
		CSVManager.initGradient(csv_file, childname);
		
		L = CostFunction.LSNZ(V, W, H);
		CSVManager.addCostGradient((long) 0, L);
		
		long iteration = 0;
		long startClock = System.nanoTime();
		
		do{
			computation();
			L = CostFunction.LSNZ(V, W, H); 
			long stop = System.nanoTime() - startClock;
			CSVManager.addCostGradient(stop, L);
			iteration++;
			
		}while(L > convergence && iteration <= max_iter);
		
		CSVManager.endGradient();
		
		return L;
	}
	
	public abstract void computation();
	
}
