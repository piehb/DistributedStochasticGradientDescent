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
	
	public static void add(long time, double cost){
		csv_writer.writeNext(""+time, (""+cost).replace(".", ","));
	}
	
	public static void endGradient() throws IOException{
		csv_writer.close();
	}
	
	public static CSVWriter csv_writer_runningTime;
	
	public static void initTest_exactValidation(String path, int m , int n ) throws IOException{
		csv_writer_runningTime = new CSVWriter(new FileWriter(path + "/m_" + m + "_n_" + n + ".csv" ), ';');
		csv_writer_runningTime.writeNext("r", "Time_GD", "Time_SGD");
	}
	
	public static void add(int r, long Time_GD, long Time_SGD){
		csv_writer_runningTime.writeNext(""+r , ""+Time_GD, ""+Time_SGD);
	}
	
	public static void endTest() throws IOException{
		csv_writer_runningTime.close();
	}
}
