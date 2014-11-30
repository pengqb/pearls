package com.hundsun.hsccbp.pearls;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

public class SortTest {
	public static final int TOTAL = 5000000;

	@Test
	public void test() {
		int[] array = SortTest.GetRandomSequence(TOTAL);
//		for(int i= 0;i<array.length;i++){
//			System.out.println(array[i]);
//		}
		long qSortStartTime = System.nanoTime();
		QuickSort qs = new QuickSort();
		int[] sortedArray = qs.sort(array);		
		long qSortEndTime = System.nanoTime();
		long qsortTime = qSortEndTime - qSortStartTime;
		//long mySortTime = 900000000;
		long mySortStartTime =  System.nanoTime();
		BitSetSort bss = new BitSetSort();
		BitSet bs = bss.sort(array);
		long mySortEndTime =  System.nanoTime();
		long mySortTime = mySortEndTime - mySortStartTime;
		
		System.out.println("qsortTime:" + qsortTime+";mySortTime"+mySortTime);
//		for(int i : sortedArray){
//			System.out.println(i);
//		}
		assertTrue(mySortTime < qsortTime);
	}
	
	
	public static int[] GetRandomSequence(int total) {
		int[] input = new int[total];
		for(int i= 0;i<total;i++){
			input[i] = i;
		}
		int m = total;
		int tmp;
		Random random = new Random();
		for(int i= 0;i<m;i++){
			int num = random.nextInt(total);
			tmp = input[i];
			input[i] = input[num];
			input[num] = tmp;
		}
		return input;
	}

}
