package com.hundsun.hsccbp.kata.primefactor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PrimeFactorsTest {

	PrimeFactors prime = new PrimeFactors();
	List<Integer> expecteds = new ArrayList<Integer>();

	@Test
	public void one_return_empty() {
		List<Integer> factors = prime.factor(1);
		assertEquals(new ArrayList<Integer>(), factors);
	}

	@Test
	public void two_return_two() {
		List<Integer> factors = prime.factor(2);
		expecteds.add(2);
		assertEquals(expecteds, factors);
	}

	@Test
	public void three_return_three() {
		List<Integer> factors = prime.factor(3);
		expecteds.add(3);
		assertEquals(expecteds, factors);
	}

	@Test
	public void four_return_two_and_two() {
		List<Integer> factors = prime.factor(4);
		expecteds.add(2);
		expecteds.add(2);
		assertEquals(expecteds, factors);
	}

	@Test
	public void six_return_two_and_three() {
		List<Integer> factors = prime.factor(6);
		expecteds.add(2);
		expecteds.add(3);
		assertEquals(expecteds, factors);
	}

	@Test
	public void eight_return_two_and_two_and_two() {
		List<Integer> factors = prime.factor(8);
		expecteds.add(2);
		expecteds.add(2);
		expecteds.add(2);
		assertEquals(expecteds, factors);
	}
	
	@Test
	public void nine_return_three_and_three() {
		List<Integer> factors = prime.factor(9);
		expecteds.add(3);
		expecteds.add(3);
		assertEquals(expecteds, factors);
	}
	
	@Test
	public void thirty_return_two_and_three_and_five() {
		List<Integer> factors = prime.factor(30);
		expecteds.add(2);
		expecteds.add(3);
		expecteds.add(5);
		assertEquals(expecteds, factors);
	}
}
