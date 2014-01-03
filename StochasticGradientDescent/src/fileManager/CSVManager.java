package fileManager;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVManager {
	
	public static CSVWriter csv_writer;
	
	public static void initGradient(String csv_file, String column_label) throws IOException{
		csv_writer = new CSVWriter(new FileWriter(csv_file), ';');
		csv_writer.writeNext("time(nanos)" , column_label);
	}
	
	public static void addCostGradient(long time, double cost){
		csv_writer.writeNext(""+time, (""+cost).replace(".", ","));
	}
	
	public static void endGradient() throws IOException{
		csv_writer.close();
	}
	
	
}
