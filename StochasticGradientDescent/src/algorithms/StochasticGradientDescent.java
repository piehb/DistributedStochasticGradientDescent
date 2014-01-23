package algorithms;
import java.io.FileNotFoundException;
import java.util.Random;


public class StochasticGradientDescent extends Gradient{
	
	double[] gradW, gradH;
	
	public StochasticGradientDescent(String v_path, String w_path, String h_path, int m, int n , int r, int max_value, double convergence, long max_iter) throws FileNotFoundException{
		super(v_path, w_path, h_path, m, n, r, max_value, convergence, max_iter);
//		this.max_iter = max_iter * 10;
		
	}
	
	public void update_W_and_H(int iz, int jz){
		
		for(int k = 0 ; k < r ; k++){
			W[iz][k] += (double) epsilon * gradW[k];
			H[k][jz] += (double) epsilon * gradH[k];
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
	public void computation(){
//		System.out.println("SGD computation");
		Random random = new Random();
		
		for(int it = 0 ; it < V.size() ; it++){ //V.size()
			Object[] keys =  V.keySet().toArray();
			String random_key = (String) keys[random.nextInt(keys.length)];
			String[] ij = random_key.split(",");
			
			int iz = Integer.parseInt(ij[0]), jz = Integer.parseInt(ij[1]);
			
			compute_gradient(iz, jz);
			update_W_and_H(iz, jz);
		}
//		System.out.println("/SGD computation");
	}	
}
