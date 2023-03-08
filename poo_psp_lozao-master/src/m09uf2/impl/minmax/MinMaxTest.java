package m09uf2.impl.minmax;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.minmax.MinMaxSolver;

public class MinMaxTest {
	
	private MinMaxCalc calc;
	private MinMaxSolver solv;

	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);
	}
	
	@BeforeEach
	void beforeEach() {		
		
		calc = new MinMaxCalc(0);
		
		String packageName = Problems.getImplPackage(MinMaxSolver.class);
		solv = Reflections.newInstanceOfType(
				MinMaxSolver.class, packageName + ".MinMaxSolverImpl",
				new Class[] {MinMaxCalc.class}, 
				new Object[] {calc});
	}
	
	@Test
	void test10Nums() {
		
		double[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};		
		double[] minMax = solv.minMax(nums);
		
		assertNotNull(minMax);		
		assertEquals(2, minMax.length);
		
		assertEquals(minMax[0], 1.0);
		assertEquals(minMax[1], 9.0);
	}
	
	@Test
	void test5000Nums() {
		
		double[] nums = calc.random(5000);
		double[] minMax = solv.minMax(nums);
		
		assertNotNull(minMax);		
		assertEquals(2, minMax.length);
		
		assertEquals(minMax[0], -403.1524998956356);
		assertEquals(minMax[1], 336.82882155765856);
	}
}
