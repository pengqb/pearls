package com.vela.iot.common.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTest {
	static Random random = new Random();
	static ThreadLocalRandom tRandom = ThreadLocalRandom.current();
	static SecureRandom sRandom;
	
	static {
		try {
			sRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {

		}
	}
	
	static int r1(int n) {
		return (int) (Math.random() * n);
	}

	static int r2(int n) {
		return random.nextInt(n);
	}
	
	static int r3(int n){
		return tRandom.nextInt(n);
	}

	static int r4(int n){
		return sRandom.nextInt(n);
	}
	
	//结论：无论从准确性和效率，Random.nextInt(n)都比Math.random()要好。
	//对安全性有要求的随机数应用情景，可以用java.security.SecureRandom。代替伪随机的Random类。
	//效率上肯定有损失，大概相差2个数量级。
	//但是在多线程的表现中，Random.nextInt(n)的性能很差。所以在Java7新增类ThreadLocalRandom，给多线程并发生成随机数使用的。
	public static void main(String[] args) {
		long t1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r1(1024);
		}
		long t2 = System.nanoTime();
		System.out.println(t2 - t1);
		
		t1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r2(1024);
		}
		t2 = System.nanoTime();
		System.out.println(t2 - t1);
		
		t1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r3(1024);
		}
		t2 = System.nanoTime();
		System.out.println(t2 - t1);
		
		t1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r4(1024);
		}
		t2 = System.nanoTime();
		System.out.println(t2 - t1);

	}

}
