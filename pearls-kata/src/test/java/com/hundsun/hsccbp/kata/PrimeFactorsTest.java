package com.hundsun.hsccbp.kata;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PrimeFactorsTest {

	PrimeFactors prime = new PrimeFactors();
	List<Integer> expected = new ArrayList<Integer>();
	@Test
	public void no_prime_factor() {
		List<Integer> factors = prime.factor(1);
		assertEquals(expected, factors);
	}

	@Test
	public void two_return_two() {
		expected.add(2);
		List<Integer> factors = prime.factor(2);
		assertEquals(expected, factors);
	}

	@Test
	public void three_return_three() {
		expected.add(3);
		List<Integer> factors = prime.factor(3);
		assertEquals(expected, factors);
	}
	
	@Test
	public void four_return_two_and_two() {
		expected.add(2);
		expected.add(2);
		List<Integer> factors = prime.factor(4);
		assertEquals(expected, factors);
	}
	
	@Test
	public void six_return_two_and_three() {
		expected.add(2);
		expected.add(3);
		List<Integer> factors = prime.factor(6);
		assertEquals(expected, factors);
	}
	
	@Test
	public void eight_return_two_and_two_and_two() {
		expected.add(2);
		expected.add(2);
		expected.add(2);
		List<Integer> factors = prime.factor(8);
		assertEquals(expected, factors);
	}
	
	@Test
	public void nine_return_three_and_three() {
		expected.add(3);
		expected.add(3);
		List<Integer> factors = prime.factor(9);
		assertEquals(expected, factors);
	}
	
	@Test
	public void oen_return_three_and_three() {
		expected.add(3);
		expected.add(3);
		expected.add(5);
		expected.add(5);
		expected.add(5);
		List<Integer> factors = prime.factor(1125);
		assertEquals(expected, factors);
	}
}
