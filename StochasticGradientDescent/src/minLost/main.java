package minLost;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			//V parameters
			int m = 800, n = 300, max_value = 5;
			
			//iteration max for algorithm
			int iteration = 2000;
			double convergence = 0.02;
			
			//create V
			System.out.println("[main] Start create V.in with " + m + " lines and " + n + " columns and values between [ 1 .. " + max_value + " ] ");
			Data.create_V("V.in", m, n, max_value);
			
			int nbr_entries = m * n;
			
			//remove one point at each iteration
			for(int nbr_missing_values = 0 ; nbr_missing_values <= 0 ; nbr_missing_values++){ //nbr_entries
				
				System.out.println("[main]create " + nbr_missing_values + " missing points");
				
				//switch missing_values 0 times
				for(int switch_missing_values_times = 0 ; switch_missing_values_times < 1 ; switch_missing_values_times++){
					
					//choose missing_values points
					ArrayList<String> missing_values = Data.create_missing_values(nbr_missing_values, m, n);
					System.out.println("[main]switch " + missing_values + " missing values");
					
					//create new V
					System.out.println("[main] create V");
					Data.create_V_with_missing_values("V.in", "V1.in", missing_values);
					
					//init W and H
					for(int r = 10 ; r <= 10 ; r++){
						
						System.out.println("[main] r = " + r);
						
						//find W H with different starting point
						for(int W_H_different_starting_point = 0 ; W_H_different_starting_point < 1 ; W_H_different_starting_point ++){
							double interval = Math.sqrt((double)max_value / r);
							
							System.out.println("[main] create W");
							double[][] W = Data.create_random_matrix(m, r, interval);
							
							System.out.println("[main] create H");
							double[][] H = Data.create_random_matrix(r, n, interval);
							
//							System.out.print("[main] : WH = ");		Matrix.printWH(W, H);		System.out.println();
							
							//compute stochastic gradient descent
							System.out.println("init sgd");
							StochasticGradientDescent sgd = new StochasticGradientDescent("V1.in", W, H, r, convergence, iteration);
							double L_sgd = sgd.run();
							System.out.println("[main/r] >> (W,H) " + W_H_different_starting_point + " SGD nbr_iteration=" + sgd.iter + " L= " + L_sgd);
							sgd = null;
							
							//compute gradient descent
							System.out.println("init gd");
							GradientDescent gd = new GradientDescent("V1.in", W, H, m, n, r, convergence, iteration);
							double L_gd = gd.run();
							System.out.println("[main/r] >> (W,H) " + W_H_different_starting_point + " GD nbr_iteration=" + gd.iter + " L= " + L_gd);
							gd = null;
						}//end for start with different W H
						
					}//end for r
				}//end for switch missing values
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
