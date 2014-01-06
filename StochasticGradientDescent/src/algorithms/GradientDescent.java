package algorithms;
import java.io.FileNotFoundException;


public class GradientDescent extends Gradient{

	double[][] gradW, gradH;
	
	public GradientDescent(String v_filename, double[][] w2, double[][] h2, int m, int n, int r, double convergence, long max_iter) throws FileNotFoundException{
		super(v_filename, w2, h2, m, n, r, convergence, max_iter);
		
	}
	
	public void compute_gradient(){
		gradW = new double[m][r];
		gradH = new double[r][n];
		
		for(int i = 0 ; i < m ; i++){
			for(int j = 0 ; j < n ; j++){
				String ij = ""+i + "," + j ;
				if(V.containsKey(ij)){
					double eij = 0;
					for(int k = 0 ; k < r ; k++){
						eij += (double) W[i][k] * H[k][j];
					}
					eij = V.get(ij) - eij;
					for(int k = 0 ; k < r ; k++){
						gradW[i][k] += (double) 2 * H[k][j] * eij ;
						gradH[k][j] += (double) 2 * W[i][k] * eij ;
					}
				}
			}
		}
	}
	
	public void update_W_and_H(){
		
		for(int i = 0 ; i < m ; i++){
			for(int k = 0 ; k < r ; k++){
				W[i][k] += (double) epsilon * gradW[i][k] ;
			}
		}
			
		for(int j = 0 ; j < n ; j++){
			for(int k = 0 ; k < r ; k++){
				H[k][j] += (double) epsilon * gradH[k][j] ;
			}
		}
	}

	@Override
	public void computation() {
		compute_gradient();
		update_W_and_H();		
	}
}
