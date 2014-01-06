package test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import fileManager.CSVManager;
import algorithms.Gradient;
import algorithms.GradientDescent;
import algorithms.StochasticGradientDescent;

public class GradientTest {

	double[][] W, H, V_tab;
	
	
	/**
	 * 1. create W and H
	 * 2. create V
	 * @throws IOException 
	 */
	public void create_V(int m, int n , int r, int max_value, String filename) throws IOException{
		
		double interval = Math.sqrt((double)max_value / r);
		
		W = DataForTest.create_random_matrix(m, r, interval);
		
		H = DataForTest.create_random_matrix(r, n, interval);
		
		V_tab = DataForTest.matrixMultiplication(W, H);
		
		DataForTest.write_matrix(filename, V_tab);
	}
	
	/**
	 * 1. create W and H
	 * 2. create V
	 * 3. GD  ( V, W, H) == 0
	 * 4. SGD ( V, W, H) == 0
	 */
	
	@Test
	public void runningTime() {
		
		System.out.println("runningTime");
		
		int m , n , max_value = 5, max_iter = 1;
		double convergence = 0.00001;
		Gradient gradient = null;
		
		for(int plot = 1 ; plot <= 3 ; plot++){
			
			n = ((int) Math.pow(10, plot));
			m = 3 * n;
			
			String path = "runningTime/csv";
			new File(path).mkdirs();
			
			String v_filename = "runningTime/v.in";
			
			try {
				CSVManager.initTest_exactValidation(path, m, n);
				
	//			System.out.println("exactValidation/create V");
				
				for(int r = 1 ; r <= n ; r++){
					
					System.out.print("m = " + m + " , n = " + n + " , r = " + r + "\t");
					
					create_V(m, n, r, max_value, v_filename);
					
					//sgd
					gradient = gradient("SGD", v_filename, m , n, r, convergence , max_iter);
					
					double L_SGD = gradient.run("SGD", path + "/L_SGD.csv");
					long Time_SGD = gradient.time/gradient.V.size();
					
					//gradient descent
					gradient = gradient("GD", v_filename, m , n, r, convergence , max_iter);
					
					
					double L_GD = gradient.run("GD", path + "/L_GD.csv");
					long Time_GD = gradient.time;
					
					
					//result
					System.out.println("time_SGD = " + Time_SGD +" , time_GD = " + Time_GD );
					
					CSVManager.add(r,Time_GD, Time_SGD);

				}//end for r
				
				CSVManager.endTest();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Gradient gradient(String gradient_type, String v_filename, int m , int n , int r , double convergence, int max_iter) throws IOException{
		
		Gradient gradient = null;
		if(gradient_type.equals("GD")){
			gradient = new GradientDescent(v_filename, W, H, m, n, r, convergence, max_iter);
		}else if(gradient_type.equals("SGD")){
			gradient = new StochasticGradientDescent(v_filename, W, H, m, n, r, convergence, max_iter);
		}
		return gradient;
	}
	
}
