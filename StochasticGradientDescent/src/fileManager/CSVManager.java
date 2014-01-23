package fileManager;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVManager {
	
	public static CSVWriter csv_writer;
	
	public static void initGradient(String csv_file, String column_label) throws IOException{
		csv_writer = new CSVWriter(new FileWriter(csv_file), ';');
		csv_writer.writeNext("time(nanos)" , column_label + "_local", column_label);
	}
	
	public static void add(long time, double cost, double cost2){
		csv_writer.writeNext(""+time, (""+cost).replace(".", ","), (""+cost2).replace(".", ","));
	}
	
	public static void endGradient() throws IOException{
		csv_writer.close();
	}
	
	public static CSVWriter csv_writer_runningTime;
	
	public static void initTest_exactValidation(String path, int m , int n ) throws IOException{
		csv_writer_runningTime = new CSVWriter(new FileWriter(path + "/m_" + m + "_n_" + n + ".csv" ), ';');
		csv_writer_runningTime.writeNext("r", "Lo", "L_GD", "L_SGD", "Time_GD", "Time_SGD");
	}
	
	public static void add(int r, double Lo, double L_GD, double L_SGD, long Time_GD, long Time_SGD){
		csv_writer_runningTime.writeNext(""+r , ""+Lo, ""+L_GD, ""+L_SGD, ""+Time_GD, ""+Time_SGD);
	}
	
	public static void endTest() throws IOException{
		csv_writer_runningTime.close();
	}
}
