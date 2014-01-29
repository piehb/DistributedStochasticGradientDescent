package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.junit.Test;

import algorithms.Gradient;
import algorithms.GradientDescent;
import algorithms.StochasticGradientDescent;
import cost.CostFunction;
import fileManager.CSVManager;
import fileManager.FileManager;

public class GradientTest {

	int MAX_VALUE = 10;
	public double[][] W, H;
	public static double[][] V_tab;
	
	@Test
	public void test_my_SGD_GD() throws IOException{
		System.out.println("[test_my_SGD_GD]");	
		int m = 3, n=3 ,  r=1;
		String data_path = "data/m_" + m + "_n_" + n + "_r_"+r;
		
		//init gradient
		Gradient gradient = null;
		
		//convergence
		double conv = 0.001;
		
		//run sgd
		gradient = gradient("SGD", data_path + "/density_1", data_path+"/w.in", data_path+"/h.in", m , n, r, MAX_VALUE, conv , 1000);
		double L_SGD = gradient.run("_test1_SGD", "", "");
		//run gd
		gradient = gradient("GD", data_path + "/density_1", data_path+"/w.in", data_path+"/h.in", m , n, r, MAX_VALUE, conv , 1000);
		double L_GD = gradient.run("_test1_GD", "", "");
		
		System.out.println((L_SGD == 0) +" , "+ (L_GD == 0) );
		
		System.out.println("[/test_my_SGD_GD]");
		
	}
	
	
public void create_V_withNoise() throws IOException{
	System.out.println("[create_V_withNoise]");	
	int m = 3, n=3 ,  r=1;
	String data_path = "data/m_" + m + "_n_" + n + "_r_"+r;
	
	//output noise_repartition noise_proportion GD_reconsctruct SGD_reconstruct
	
	double[] noise_proportion_tab = {0.01, 0.15, 0.3, 0.5, 0.75, 0.9};
	int[] noise_repartition_tab= {0, 1, 2, 3, 4, 5};
	
	//init gradient
	Gradient gradient = null;
	//test path
	new File("test2/temp").mkdirs();
	String path = "test2/";
	
	//delete file if exists
	File V_File = new File("test2/"+path);
	if(V_File.exists()){
		V_File.delete(); 
	}
	V_File.createNewFile();
	
	FileWriter out_fw = new FileWriter(V_File);
	
	for(int noise_repartition : noise_repartition_tab ){
		for(double noise_proportion: noise_proportion_tab){
			System.out.print("[ " + noise_repartition + " , " + noise_proportion + " ] ");
			//create new W & H
			String w_h_directory = "test2/temp"; new File(w_h_directory).mkdirs();
			pertubation(w_h_directory, noise_proportion, noise_repartition, data_path + "/w.in", data_path + "/h.in");
			//run sgd
			gradient = gradient("SGD", data_path + "/density_1.0", w_h_directory+"/w.in", w_h_directory+"/h.in", m , n, r, MAX_VALUE, 0 , 100);
			double L_SGD = gradient.run("_SGD_rep_" + noise_repartition + "_prop_" + noise_proportion, "", "");				
			//run gd
			gradient = gradient("GD", data_path + "/density_1.0", w_h_directory+"/w.in", w_h_directory+"/h.in", m , n, r, MAX_VALUE, 0 , 100);
			double L_GD = gradient.run("_GD_" + noise_repartition + "_" + noise_proportion, "", "");
			
			String out = noise_repartition +" "+ noise_proportion +" "+ (L_SGD == 0) +" "+ (L_GD == 0) ;
			out_fw.write(out + "\n");
		}
	}
	
	out_fw.close();
	System.out.println("[/create_V_withNoise]");
	
	
}
	
	private void pertubation(String w_h_directory, double noise_proportion,
			int noise_repartition, String input_w, String input_h) throws IOException {
		// TODO Auto-generated method stub
		
		//init W output
		Scanner w_sc = new Scanner(new File(input_w));
		String w_firstline = w_sc.nextLine();
		String[] w_m_n = w_firstline.split(" ");
		int w_changes = (int) (Integer.parseInt(w_m_n[0]) * Integer.parseInt(w_m_n[1]) * noise_proportion);
		FileWriter w_fw = new FileWriter(new File(w_h_directory + "/w.in")); 
		w_fw.write(w_firstline + "\n");
		while(w_changes > 0){
			String[] element = w_sc.nextLine().split(" ");
			double noise = new Random().nextGaussian() * noise_repartition;
			float new_value = (float) (Double.parseDouble(element[1]) + noise) ;
			w_fw.write(element[0] + " " + new_value + "\n");
			w_changes--;
		}
		while(w_sc.hasNext()){
			w_fw.write( w_sc.nextLine() + "\n" );
		}
		w_sc.close();
		w_fw.close();
		
		//init H output
		Scanner h_sc = new Scanner(new File(input_h));
		String h_firstline = h_sc.nextLine();
		String[] h_m_n = h_firstline.split(" ");
		int h_changes = (int) (Integer.parseInt(h_m_n[0]) * Integer.parseInt(h_m_n[1]) * noise_proportion);
		FileWriter h_fw = new FileWriter(new File(w_h_directory + "/h.in")); 
		h_fw.write(h_firstline + "\n");
		while(h_changes > 0){
			String[] element = h_sc.nextLine().split(" ");
			double noise = new Random().nextGaussian() * noise_repartition;
			float new_value = (float) (Double.parseDouble(element[1]) + noise) ;
			h_fw.write(element[0] + " " + new_value + "\n");
			h_changes--;
		}
		while(h_sc.hasNext()){
			h_fw.write( h_sc.nextLine() + "\n" );
		}
		h_sc.close();
		h_fw.close();
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
		System.out.println("conv = " + convergence);
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
	
	
	public void test_SGD_GD() throws IOException {
		
		System.out.println("test_SGD_GD");
		
		int m=300, n=20, r=5, max_value = 10;
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
			
			String[] sparse_files= { "/density_0.01", "/density_0.1", "/density_0.25", "/density_0.5", "/density_0.75"};
//			String[] sparse_files= { "/density_0.5" };
			for(String V_sparse : sparse_files){
				//sgd
				System.out.println("start sgd " + path + " " + V_sparse);
				gradient = gradient("SGD", path + V_sparse, w_path, h_path, m , n, r, max_value, convergence , max_iter);
				gradient.run("_SGD_" + dif, path + V_sparse + "/L_SGD.csv", path + "/density_1.0");				
				System.out.println("end sgd");
				
				//gradient descent
				/*System.out.println("start gd " + path + " " + V_sparse);
				gradient = gradient("GD", path + V_sparse, w_path, h_path,  m , n, r, max_value, convergence , max_iter);
				gradient.run("_GD_" + dif, path + V_sparse + "/L_GD.csv", path + "/density_1.0");				
				System.out.println("end gd");*/
			}
		}//end for diff
		System.out.println("finish");
	}
}
