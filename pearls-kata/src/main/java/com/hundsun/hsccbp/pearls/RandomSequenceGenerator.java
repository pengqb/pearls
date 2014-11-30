package com.hundsun.hsccbp.pearls;

import java.util.Random;

/**
 * 不重复随机数列生成器<br>
 * 参考博文 http://www.cnblogs.com/eaglet/archive/2011/01/17/1937083.html
 * 
 * @author pengqb
 * 
 */
public class RandomSequenceGenerator {
	public static final int TOTAL = 10;
	public static int[] GetRandomSequence0(int total) {
		int[] hashtable = new int[total];
		int[] output = new int[total];
		Random random = new Random();
		for (int i = 0; i < total; i++) {
			int num = random.nextInt(total);
			while (hashtable[num] > 0) {
				num = random.nextInt(total);
			}
			output[i] = num;
			hashtable[num] = 1;
		}
		return output;
	}

	public static int[] GetRandomSequence1(int total) {
		int[] input = new int[total];
		for (int i = 0; i < total; i++) {
			input[i] = i;
		}
		int[] output = new int[total];
		Random random = new Random();
		int end = total-1;
		for (int i = 0; i < total; i++) {
			int num = random.nextInt(end +1);
			output[i] = input[num];
			input[num] = input[end];
			end--;
		}
		return output;
	}
	
	public static int[] GetRandomSequence2(int total) {
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
