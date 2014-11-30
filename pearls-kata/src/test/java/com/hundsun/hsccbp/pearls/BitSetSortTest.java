package com.hundsun.hsccbp.pearls;

import java.util.BitSet;

import junit.framework.TestCase;

public class BitSetSortTest extends TestCase {

	public void testSort() {
		int[] array = RandomSequenceGenerator.GetRandomSequence2(RandomSequenceGenerator.TOTAL);
		//int[] array = {3,8,5,2,9,6,7};
		for(int i= 0;i<array.length;i++){
			System.out.println(array[i]);
		}
		long sortStartTime = System.nanoTime();
		BitSetSort bss = new BitSetSort();
		BitSet bs = bss.sort(array);
		long sortEndTime = System.nanoTime();
		System.out.println(sortEndTime - sortStartTime);
		//System.out.println("bs.length=" +bs.length()+ ";bs.size="+ bs.size());
		for(int i = 0; i<bs.length();i++){
			if(bs.get(i))
				System.out.println(i);
		}	
		//fail("Not yet implemented");
	}

}
