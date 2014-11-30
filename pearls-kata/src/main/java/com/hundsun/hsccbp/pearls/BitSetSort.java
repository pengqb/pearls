package com.hundsun.hsccbp.pearls;

import java.util.BitSet;

public class BitSetSort {
	public BitSet sort(int[] a){
		BitSet bs = new BitSet(RandomSequenceGenerator.TOTAL);
		bs.clear();
		for(int i : a){
			bs.set(i);
		}
		return bs;
	}

}
