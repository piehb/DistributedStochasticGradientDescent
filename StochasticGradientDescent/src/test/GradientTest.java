package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import cost.CostFunction;
import algorithms.Gradient;
import algorithms.GradientDescent;
import algorithms.StochasticGradientDescent;
import fileManager.CSVManager;
import fileManager.FileManager;

public class GradientTest {

	public double[][] W, H;
	public static double[][] V_tab;
	
	
	/**
	 * 1. create W and H
	 * 2. create V
	 * @throws IOException 
	 */
	public void create_V(int m, int n , int r, int max_value, String filename) throws IOException{
		
		double interval = Math.sqrt((double) max_value / r);
		
		W = DataForTest.create_random_matrix(m, r, interval);
		
		H = DataForTest.create_random_matrix(r, n, interval);
		
		V_tab = DataForTest.matrixMultiplication(W, H, false);
		
		DataForTest.write_matrix(filename, V_tab);
	}
	
public void create_V_withNoise(int m, int n , int r, int max_value, String path) throws IOException{
		
	System.out.println("[create_V_withNoise]");
	
	double interval = Math.sqrt((double)max_value / r);
	
	if(!new File(path + "/w_.in").exists()){
		W = DataForTest.create_random_matrix(m, r, interval);
		DataForTest.write_matrix(path + "/w_.in", W);
	}else{
		W = FileManager.read_Matrix(path + "/w_.in");
	}
	
	System.out.println("end W");
	
	if(!new File(path + "/h_.in").exists()){
		H = DataForTest.create_random_matrix(r, n, interval);
		DataForTest.write_matrix(path + "/h_.in", H);
	}else{
		H = FileManager.read_Matrix(path + "/h_.in");
	}
	
	System.out.println("end H");
	
	if(!new File(path + "/v.in").exists()){
		V_tab = DataForTest.matrixMultiplication(W, H, path + "/v.in", max_value);
	}else{
		V_tab = FileManager.read_Matrix(path + "/v.in");
	}
//	V_tab = DataForTest.matrixMultiplication(W, H, false);
	
	System.out.println("[/create_V_withNoise]");
}
	
	/**
	 * 1. create W and H
	 * 2. create V
	 * 3. GD  ( V, W, H) == 0
	 * 4. SGD ( V, W, H) == 0
	 */
	
	
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
					System.out.println("start gd");
					gradient = gradient("SGD", v_filename, m , n, r, max_value, convergence , max_iter);
					
					double L_SGD = gradient.run("SGD", path + "/L_m_" +m+ "_n" +n+ "_r_" +r+ "_SGD.csv");
					long Time_SGD = gradient.time;
					
					System.out.println("end sgd");
					
					//gradient descent
					System.out.println("start gd");
					gradient = gradient("GD", v_filename, m , n, r, max_value, convergence , max_iter);
					
					
					double L_GD = gradient.run("GD", path + "/L_m_" +m+ "_n" +n+ "_r_" +r+ "_GD.csv");
					long Time_GD = gradient.time;
					
					System.out.println("end sgd");
					
					//at initial
					double Lo = CostFunction.LSNZ(gradient.V, W, H, max_value);
					
					//result
					System.out.println("time_SGD = " + Time_SGD +" , time_GD = " + Time_GD );
					
					CSVManager.add(r, Lo, L_GD, L_SGD, Time_GD, Time_SGD);
					
				}//end for r
				
				CSVManager.endTest();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Gradient gradient(String gradient_type, String v_filename, String w_filename, String h_filename, int m , int n , int r , int max_value, double convergence, int max_iter) throws IOException{
		
		Gradient gradient = null;
		if(gradient_type.equals("GD")){
			gradient = new GradientDescent(v_filename, w_filename, h_filename, m, n, r, max_value, convergence, max_iter);
		}else if(gradient_type.equals("SGD")){
			gradient = new StochasticGradientDescent(v_filename, w_filename, h_filename, m, n, r,max_value, convergence, max_iter);
		}
		return gradient;
	}
	
	/**
	 * 1. create W and H
	 * 2. create V
	 * 3. V' = V + noise
	 * 3. GD  ( V', W, H)
	 * 4. SGD ( V', W, H)
	 */

	
	public void runningTime_withNoise() {
		
		System.out.println("runningTime_withNoise");
		
		int m , n , max_value = 10, max_iter = 100;
		double convergence = 0;
		Gradient gradient = null;
		
		for(int plot = 1 ; plot <= 1 ; plot++){
			
			n = 10; //((int) Math.pow(10, plot));
			m = 30; //3 * n;
			
			String path = "runningTime_withNoise/csv";
			new File(path).mkdirs();
			
			try {
				CSVManager.initTest_exactValidation(path, m, n);
				
	//			System.out.println("exactValidation/create V");
				
				for(int r = 1 ; r <= 1 ; r++){
					
					System.out.print("m = " + m + " , n = " + n + " , r = " + r + "\t");
					
					float[] sparsity = { 1, 5, 15, 50, 75, 90}; 

					int sparse = (int) (m * n * sparsity[0] * 0.01);
					
					create_V_withNoise(m, n, r, max_value,path);
					
					DataForTest.create_sparseMatrix(m, n, sparse, path+"/v.in");
					
					//sgd
					System.out.println("start sgd");
					gradient = gradient("SGD", path + "/v.in", m , n, r, max_value, convergence , max_iter);
					
					double L_SGD = gradient.run("SGD", path + "/m_" +m+ "_n" +n+ "_r_" +r+ "L_SGD.csv");
					long Time_SGD = gradient.time;
					
					System.out.println("end sgd");
					
					//gradient descent
					System.out.println("start gd");
					gradient = gradient("GD", path + "/v.in", m , n, r, max_value, convergence , max_iter);
					
					
					double L_GD = gradient.run("GD", path + "/m_" +m+ "_n" +n+ "_r_" +r+ "L_GD.csv");
					long Time_GD = gradient.time;
					
					System.out.println("end gd");
					
					//at initial
					double Lo = CostFunction.LSNZ(gradient.V, W, H, max_value);
					
					//result
					System.out.println("Lo = " + Lo + " , L_SGD = " + L_SGD +" , L_GD = " + L_GD );
					System.out.println("time_SGD = " + Time_SGD +" , time_GD = " + Time_GD );
					
					CSVManager.add(r, Lo, L_GD, L_SGD, Time_GD, Time_SGD);

				}//end for r
				
				CSVManager.endTest();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void create_repo() throws IOException {
		
		System.out.println("create_repo");
		
		int m=3000, n=1000, r=15, max_value = 10;
		long space = 0L;
		String path = "data/m_" +m+ "_n_" +n+ "_r_" +r;
		new File(path).mkdirs();
		
		double interval = Math.sqrt((double)max_value / (2*r));
		
		//create Wopt
		String w_path = path + "/w.in" ;
		double[][] W;
		if(!new File(w_path).exists()){
			W = DataForTest.create_random_matrix(m, r, interval);
			DataForTest.write_matrix(w_path, W);
		}else{
			W = FileManager.read_Matrix(w_path);
		}
		System.out.println("W : space = " + new File(w_path).length());
		space += new File(w_path).length();
		
		//create Hopt
		String h_path = path + "/h.in" ;
		double[][] H;
		if(!new File(h_path).exists()){
			H = DataForTest.create_random_matrix(r, n, interval);
			DataForTest.write_matrix(h_path, H);
		}else{
			H = FileManager.read_Matrix(h_path);
		}
		System.out.println("H : space = " + new File(h_path).length());
		space += new File(h_path).length();
		
		//create Vopt
		String v_path = path + "/density_100";
		
		if(!new File(v_path).exists()){
			
			DataForTest.matrixMultiplication(W, H, v_path, max_value);
			
//			System.out.println("V[0][1] : " + V[0][1]);
		}
		
		System.out.println("V : density = 100 : space = " + new File(v_path).length());
		space += new File(v_path).length();
		
		//sparse matrix
		double[] density = { 0.9, 0.75, 0.5, 0.25, 0.1, 0.01};
		for(double dense : density){
			String v_sparse_path = path + "/density_" + (dense * 100);
			if(!new File(v_sparse_path).exists()){
				int entriesNb = (int) (m * n * dense);
				DataForTest.create_sparseMatrix(m, n, v_path, entriesNb, v_sparse_path);
				System.out.println("V : density = " + (dense*100) + " :  items = " + entriesNb + " : space = " + new File(v_sparse_path).length());
				space += new File(v_sparse_path).length();
			}
		}
		System.out.println("total space = " + space);
	}
	
	@Test
	public void test_SGD_GD() throws IOException {
		
		System.out.println("test_SGD_GD");
		
		int m=300, n=100, r=10, max_value = 10;
		String path = "data/m_" +m+ "_n_" +n+ "_r_" +r;
//		new File(path).mkdirs();
		
		double interval = Math.sqrt((double)max_value / (2*r));
		
		double convergence = 0; int max_iter = 10000;
		Gradient gradient = null;
		
		for(int dif=0; dif<1 ; dif++){
			//
			new File(path + "/temp").mkdir();
			//create W
			String w_path = path + "/temp/w.in" ;
			DataForTest.write_matrix(w_path, DataForTest.create_random_matrix(m, r, interval));
			
			//create H
			String h_path = path + "/temp/h.in" ;
			DataForTest.write_matrix(h_path, DataForTest.create_random_matrix(r, n, interval));
			
			String[] sparse_files= { "/density_0.01", "/density_0.1", "/density_0.25", "/density_0.5", "/density_0.75", "/density_0.9", "/density_1.0"};
			
			for(String V_sparse : sparse_files){
				//sgd
				System.out.println("start sgd " + path + " " + V_sparse);
				gradient = gradient("SGD", path + V_sparse, w_path, h_path, m , n, r, max_value, convergence , max_iter);
				gradient.run("_SGD_" + dif, path + V_sparse + "/L_SGD.csv", path + "/density_1.0");				
				System.out.println("end sgd");
				
				//gradient descent
				System.out.println("start gd " + path + " " + V_sparse);
				gradient("GD", path + V_sparse, w_path, h_path,  m , n, r, max_value, convergence , max_iter);
				gradient.run("_GD_" + dif, path + V_sparse + "/L_GD.csv", path + "/density_1.0");				
				System.out.println("end gd");
			}
		}//end for diff
		System.out.println("finish");
	}
}
