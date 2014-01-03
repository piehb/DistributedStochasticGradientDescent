package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class FileManager {
	public static HashMap<String, Double> read_V(String v_filename) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(v_filename));
		HashMap<String, Double> V = new HashMap<String, Double>();
		sc.nextLine();		// first line is <row col>
		while(sc.hasNext()){
			String[] ij_Vij = sc.nextLine().split(" ");
			Double Vij = Double.parseDouble(ij_Vij[1]);
			V.put(ij_Vij[0], Vij);
		}
		sc.close();
		return V;
	}
}
