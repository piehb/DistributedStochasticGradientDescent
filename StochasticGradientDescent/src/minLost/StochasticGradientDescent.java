package minLost;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVWriter;


public class StochasticGradientDescent {

	String filename;
	public int m, n, r, N=0;
	Map<String, Double> V;
	double[][] W, H;
	double[] gradW, gradH;
	double epsilon = 0.005, convergence;
	long iter = 1, max_iter;
	
	public StochasticGradientDescent(String v_filename, double[][] w2, double[][] h2, int r2, double convergence, long max_iter) throws FileNotFoundException{
		
//		System.out.println("\n[SGD]init stochastic gradient descent\n");

		r = r2;
		this.max_iter = max_iter;
		this.convergence = convergence;
		read_V(v_filename);
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
		
		CSVWriter writer = new CSVWriter(new FileWriter("sgd.csv"), ';');
		
		double L;
		L = Matrix.LSNZ(V, W, H);
		
		writer.writeNext("L_SGD", "time(nanos)");
		writer.writeNext((""+L).replace(".", ","),"0");
		System.out.println("[sgd/run] Lo = " + L);
//		System.out.println("[sgd/run] V = " + V);
//		System.out.print("[SGD/run] start : WH = ");			Matrix.printWH(W, H);		System.out.println();
		long startClock = System.nanoTime();
		do{
			Random random = new Random();
			for(int it = 0 ; it < N ; it++){
				int iz,jz;
				String ij ;
				do{
					iz = random.nextInt(m);
					jz = random.nextInt(n);
					ij = "" + iz + "," + jz;
				}while(!V.containsKey(ij));
				
				compute_gradient(iz, jz);
				update_WH(iz, jz, iter);
			}
			
			L = Matrix.LSNZ(V, W, H); 
			long stop = System.nanoTime() - startClock;
			writer.writeNext((""+L).replace(".", ","),""+ stop);
//			System.out.println("[SGD/"+ iter + "] L = " + L);
			
			iter++;
		}while(L > convergence && iter <= max_iter);
		
//		System.out.print("[SGD/run] : WH = ");			Matrix.printWH(W, H);		System.out.println();
		writer.close();
		
		CSVWriter writer_WH = new CSVWriter(new FileWriter("sgd_WH.csv"), ';');
		writer_WH.writeNext("WH_SGD");
		Matrix.printWH(W, H, writer_WH);
		writer_WH.close();
		
		return L;
	}
	
	public void update_WH(int iz, int jz, long iter){
		
		for(int k = 0 ; k < r ; k++){
//			System.out.println("[SGD/update_WH] before W[iz][k] = " + W[iz][k] + "    grad = " + gradW[k] + " epsilon = " + epsilon + " N = " + N);
			W[iz][k] += (epsilon * gradW[k] ) ;
//			System.out.println("[GD/update_WH] after W[iz][k] = " + W[iz][k] );
		}

		for(int k = 0 ; k < r ; k++){
//			System.out.println("[SGD/update_WH] before H[k][jz] = " + H[k][jz] + "    grad = " + gradH[k]);
			H[k][jz] += (epsilon * gradH[k] ) ;
//			System.out.println("[SGD/update_WH] after H[k][jz] = " + H[k][jz]);
		}
	
	}
	
	public void compute_gradient(int iz, int jz){
		gradW = new double[r];
		gradH = new double[r];
		
		String ij = ""+iz + "," + jz ;
		if(V.containsKey(ij)){
			double eij = 0;
			for(int k = 0 ; k < r ; k++){
				eij += W[iz][k] * H[k][jz];
			}
			eij = V.get(ij) - eij;
//			System.out.println("[SGD/cmpute_gradient] e" + ij + "=" + eij);
			for(int k = 0 ; k < r ; k++){
				gradW[k] += 2 * H[k][jz] * eij ;
				gradH[k] += 2 * W[iz][k] * eij ;
			}
		}	
	}
	
	public void read_V(String v_filename) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(v_filename));
		String m_n_ = sc.nextLine();
		String[] m_n = m_n_.split(" ");
		m = Integer.parseInt(m_n[0]);
		n = Integer.parseInt(m_n[1]);
		
		V = new HashMap<String, Double>();
		while(sc.hasNext()){
			String ij_Vij_ = sc.nextLine();
			String[] ij_Vij = ij_Vij_.split(" ");
//			System.out.println("[sgd/read_v] ij_Vij=" + ij_Vij_);
			double Vij = Double.parseDouble(ij_Vij[1]);
			V.put(ij_Vij[0], Vij);
			N++;
		}
	}
}
