package com.hundsun.hsccbp.kata.primefactor;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {
	public List<Integer> factor(Integer n) {
		List<Integer> candidates = new ArrayList<Integer>();
		Integer num = n;
		for (Integer candidate = 2; candidate <= n; candidate++) {
			while (num % candidate == 0) {
				candidates.add(candidate);
				num = num / candidate;
			}
		}
		return candidates;
	}
}
