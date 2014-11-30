package com.hundsun.hsccbp.pearls;

import junit.framework.TestCase;

public class QuickSortTest extends TestCase {

	public void testSort() {		
		int[] array = RandomSequenceGenerator.GetRandomSequence2(RandomSequenceGenerator.TOTAL);
//		for(int i= 0;i<array.length;i++){
//			System.out.println(array[i]);
//		}
		long sortStartTime = System.nanoTime();
		QuickSort qs = new QuickSort();
		int[] sortedArray = qs.sort(array);		
		long sortEndTime = System.nanoTime();
		System.out.println(sortEndTime - sortStartTime);
//		for(int i : sortedArray){
//			System.out.println(i);
//		}
		//fail("Not yet implemented");
	}

}
