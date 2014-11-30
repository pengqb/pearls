package com.hundsun.hsccbp.kata;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {
	public List<Integer> factor(Integer n) {
		List<Integer> candidates = new ArrayList<Integer>();
		Integer num = n;
		Integer candidate = 2;
		while (candidate <= num) {
			while (num % candidate == 0) {
				candidates.add(candidate);
				num /= candidate;
			}
			candidate ++;
		}
		return candidates;
	}
}
