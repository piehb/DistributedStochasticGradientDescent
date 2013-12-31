package test;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import minLost.Data;
import minLost.GradientDescent;
import minLost.Matrix;
import minLost.StochasticGradientDescent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MatrixTest {

	static int m = 800, n = 300, r = 10;
	static double[][] W, H;
	int iteration = 200;
	
	StochasticGradientDescent sgd;
	GradientDescent gd;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		int max_value = 5;
		double interval = Math.sqrt((double)max_value / r);
		
		System.out.println("[main] create W");
		W = Data.create_random_matrix(m, r, interval);
		
		System.out.println("[main] create H");
		H = Data.create_random_matrix(r, n, interval);
		
		System.out.println("[main] create WH");
		double[][] WH = Matrix.getWH(W, H);
		
		Data.create_V_test("V_test.in", WH, m, n);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ArrayList<String> missing_values = Data.create_missing_values(0, m, n);
		Data.create_V_with_missing_values("V_test.in", "V1_test.in", missing_values);
		shuffle_W_H();
		sgd = new StochasticGradientDescent("V1_test.in", W, H, r, 0.005, iteration);
		gd = new GradientDescent("V1_test.in", W, H, m, n, r, 0.0005, iteration);
	}

	private void shuffle_W_H() {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void test() {
//		double L = sgd.
		fail("Not yet implemented");
	}
	
}
