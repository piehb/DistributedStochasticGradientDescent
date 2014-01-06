package algorithms;
import java.io.FileNotFoundException;
import java.util.Random;

import cost.CostFunction;
import fileManager.CSVManager;


public class StochasticGradientDescent extends Gradient{
	
	double[] gradW, gradH;
	
	public StochasticGradientDescent(String v_filename, double[][] w2, double[][] h2, int m, int n , int r, double convergence, long max_iter) throws FileNotFoundException{
		super(v_filename, w2, h2, m, n, r, convergence, max_iter);
		
	}
	
	public void update_W_and_H(int iz, int jz){
		
		for(int k = 0 ; k < r ; k++){
			W[iz][k] += (double) epsilon * gradW[k] ;
			H[k][jz] += (double) epsilon * gradH[k] ;
		}
	}
	
	public void compute_gradient(int iz, int jz){
		gradW = new double[r];
		gradH = new double[r];
		
		String ij = ""+iz + "," + jz ;
		if(V.containsKey(ij)){
			double eij = 0;
			for(int k = 0 ; k < r ; k++){
				eij += (double) W[iz][k] * H[k][jz];
			}
			eij = V.get(ij) - eij;
//			System.out.println("[SGD/cmpute_gradient] e" + ij + "=" + eij);
			for(int k = 0 ; k < r ; k++){
				gradW[k] += (double) 2 * H[k][jz] * eij ;
				gradH[k] += (double) 2 * W[iz][k] * eij ;
			}
		}	
	}
	
	@Override
	public double computation(){
//		System.out.println("SGD computation");
		Random random = new Random();
		
		long startClock = System.nanoTime();
		
		double L = 0;
		
		for(int it = 0 ; it < V.size() ; it++){
			Object[] keys =  V.keySet().toArray();
			String random_key = (String) keys[random.nextInt(keys.length)];
			String[] ij = random_key.split(",");
			
			int iz = Integer.parseInt(ij[0]), jz = Integer.parseInt(ij[1]);
			
			compute_gradient(iz, jz);
			update_W_and_H(iz, jz);
			
			time = System.nanoTime() - startClock;
			L = CostFunction.LSNZ(V, W, H);
			CSVManager.add(time, L);
		}
//		System.out.println("/SGD computation");
		 
		return L;
	}	
}
