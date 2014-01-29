package algorithms;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import cost.CostFunction;
import fileManager.FileManager;

public abstract class Gradient {

	String v_path;
	public int m, n, r, max_value;
	public Map<String, Double> V;
	double[][] W, H;
	double epsilon = 0.0000000007, convergence;
	long max_iter;
	
	public long time;
	
	
	public Gradient(String v_path, String w_path, String h_path, int m, int n, int r, int max_value, double convergence, long max_iter) throws FileNotFoundException{
		this.m = m;
		this.n = n;
		this.r = r;
		this.max_value = max_value;
		this.max_iter = max_iter;
		this.convergence = convergence;
		this.v_path = v_path;
		
		V = FileManager.read_V(v_path);
		W = FileManager.read_Matrix(w_path);
		H = FileManager.read_Matrix(h_path);
		
	}
	
	public double run(String childname, String csv_file, String v_opt) throws IOException{
		
//		CSVManager.initGradient(csv_file, childname);
		
		double L = CostFunction.LSNZ(V, W, H, max_value);
		if(L < convergence )	L = convergence;
		double[][] V_opt = FileManager.read_v(v_opt, m, n);
		float L_ = CostFunction.LSNZ(V_opt, W, H, max_value);
		
		FileWriter fw = new FileWriter(v_path + childname + ".dat");
		
//		CSVManager.add((long) 0, L, L_);
		
		long iteration = 1;
		fw.write(iteration + " " + 0 + " " +  L + " " + L_ + "\n");
//		
		long startClock = System.nanoTime(), wait_before_computation = startClock;
//		System.out.println("end computation : " + L + ", " + L_);
		do{
			
			wait_before_computation -= System.nanoTime();
			
			computation();
			
			time = System.nanoTime() - startClock - wait_before_computation;
			wait_before_computation = System.nanoTime();
			
			L = CostFunction.LSNZ(V, W, H, max_value);
			
//			L_ = CostFunction.LSNZ(V_opt, W, H, max_value);
			
//			System.out.println("end computation : " + L + " " + L_);
//			CSVManager.add(time, L, L_);
			if(L < convergence )	L = convergence;
			fw.write(iteration + " " + time + " " +  L + " " + L_ + "\n");
			iteration++;
//			System.out.println("GD iteration : " + iteration);
			
		}while(L > convergence && iteration <= max_iter);
		
		L_ = CostFunction.LSNZ(V_opt, W, H, max_value);
		System.out.println("end computation : " + L + ", " + L_);
		fw.write(iteration + " " + time + " " + L + " " + L_ + "\n");
//		CSVManager.add(time, L, L_);
		
//		CSVManager.endGradient();
		
		fw.close();
		return L;
	}
	
	public abstract void computation();
	
}
