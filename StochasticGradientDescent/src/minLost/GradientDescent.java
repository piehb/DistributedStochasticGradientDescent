package minLost;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;


public class GradientDescent {

	String filename;
	public int m, n, r, N;
	Map<String, Double> V;
	double[][] W, H, gradW, gradH;
	double epsilon = 0.0005, convergence;
	long iter = 1, max_iter;
	
	public GradientDescent(String v_filename, double[][] w2, double[][] h2, int m2, int n2, int r2, double convergence, long max_iter) throws FileNotFoundException{
		
//		System.out.println("\n[GD]init gradient\n");
		
		m = m2;
		n = n2;
		r = r2;
		this.max_iter = max_iter;
		this.convergence = convergence;
		V = Data.read_V(v_filename);
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
	
	public double run() throws IOException{
		double L;
		L = Matrix.LSNZ(V, W, H);
		System.out.println("[gd/run] Lo = " + L);
		
		CSVWriter writer = new CSVWriter(new FileWriter("gd.csv"), ';');
		writer.writeNext("L_GD", "time(nanos)");
		writer.writeNext((""+L).replace(".", ","),"0");
		
		long startClock = System.nanoTime();
		do{
			compute_gradients();
			update_WH(iter);
			
			L = Matrix.LSNZ(V, W, H); 
			long stop = System.nanoTime() - startClock;
			writer.writeNext((""+L).replace(".", ","),""+ stop);
//			System.out.println("[GD/"+ iter + "] L = " + L);
			
			iter++;
			
		}while(L > convergence && iter <= max_iter);
		writer.close();
//		System.out.print("[GD/run] : WH = ");			Matrix.printWH(W, H);		System.out.println();
		
		CSVWriter writer_WH = new CSVWriter(new FileWriter("gd_WH.csv"), ';');
		writer_WH.writeNext("WH_GD");
		Matrix.printWH(W, H, writer_WH);
		writer_WH.close();
		
		return L;
	}
	
	public void compute_gradients(){
		gradW = new double[m][r];
		gradH = new double[r][n];
		
		for(int i = 0 ; i < m ; i++){
			for(int j = 0 ; j < n ; j++){
				String ij = ""+i + "," + j ;
				if(V.containsKey(ij)){
					double eij = 0;
					for(int k = 0 ; k < r ; k++){
						eij += W[i][k] * H[k][j];
					}
					eij = V.get(ij) - eij;
					for(int k = 0 ; k < r ; k++){
						gradW[i][k] += 2 * H[k][j] * eij ;
						gradH[k][j] += 2 * W[i][k] * eij ;
					}
				}
			}
		}
	}
	
	public void update_WH(long iter){
		
		for(int i = 0 ; i < m ; i++){
			for(int k = 0 ; k < r ; k++){
//				System.out.println(i + " " + k + "=" + W[i][k] + "   grad=" + gradW[i][k] + " eps=" + epsilon +" tot=" + (epsilon * gradW[i][k] ));
				W[i][k] += (double)epsilon * gradW[i][k] ;
			}
		}
			
		for(int j = 0 ; j < n ; j++){
			for(int k = 0 ; k < r ; k++){
				H[k][j] += (double)epsilon * gradH[k][j] ;
			}
		}
		
	}
}
