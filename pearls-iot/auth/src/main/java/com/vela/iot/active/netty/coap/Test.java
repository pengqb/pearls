package com.vela.iot.active.netty.coap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException,
			InstantiationException, IllegalAccessException {
		// Integer binary = 0b11;
		// System.out.println(binary);
		// System.out.println(Integer.toBinaryString(binary));
		// 从高位到低位分别表示:
		// 第一位，是否打印异步日志；第二位是否使用jedis写redis；第三位使用使用Lettuce写redis；第四位是否使用mongodb
		// String bin = "11000000";
		// BitSet bits = new BitSet(8);
		// for(int i=0;i< bin.length();i++){
		// if(bin.charAt(i) == '1')
		// bits.flip(i);
		// }
		//
		// for(int i=0;i< bin.length();i++){
		// System.out.println(bits.get(i));
		// }

		// ServerSocket serverSocket = new ServerSocket(0); // 读取空闲的可用端口
		// int port = serverSocket.getLocalPort();
		// System.out.println("系统分配的端口号 port=" + port);

		// LongAdder count = new LongAdder();
		// count.add(45);
		// System.out.format("处理请求数%d,处理时间%d",count.longValue(),System.nanoTime()
		// );

		// Map<String, Integer> map = new HashMap();
		// long writeStartTime = System.nanoTime();
		// for (int i = 0; i < 10000; i++) {
		// map.put(String.valueOf(i), i);
		// }
		// long writeEndTime = System.nanoTime();
		// System.out
		// .printf("共%d次写入,累计耗时%d\n", 10000, writeEndTime - writeStartTime);
		// long readStart = System.nanoTime();
		// for (int i = 0; i < 10000; i++) {
		// Integer value = map.get(i);
		// }
		// long readEnd = System.nanoTime();
		// System.out.printf("共%d次读,累计耗时%d\n", 10000, readEnd - readStart);
		//
		// long newStart = System.nanoTime();
		// for (int i = 0; i < 10000; i++) {
		// String string = new String();
		// }
		// long newEnd = System.nanoTime();
		// System.out.printf("共%d次new,累计耗时%d\n", 10000, newEnd - newStart);
		//
		// long reflectionStart = System.nanoTime();
		// for (int i = 0; i < 10000; i++) {
		// String string = String.class.newInstance();
		// }
		// long reflectionEnd = System.nanoTime();
		// System.out.printf("共%d次反射,累计耗时%d\n", 10000, reflectionEnd -
		// reflectionStart);
		//
		// long timeStart = System.nanoTime();
		// for (int i = 0; i < 10000; i++) {
		// System.nanoTime();
		// }
		// long timeEnd = System.nanoTime();
		// System.out.printf("共%d次取时间,累计耗时%d\n", 10000, timeEnd - timeStart);

		List<Integer> arrayList = new ArrayList<Integer>();
		List<Integer> linkedList = new LinkedList<Integer>();

		for (int i = 0; i < 100000; i++) {
			arrayList.add(i);
			linkedList.add(i);
		}

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < arrayList.size(); i++) {
			arrayList.get(i);
		}
		System.out.println("ArrayList for遍历速度："
				+ (System.currentTimeMillis() - startTime) + "ms");

		startTime = System.currentTimeMillis();
		for (Integer integer : arrayList) {
		}
		System.out.println("ArrayList foreach遍历速度："
				+ (System.currentTimeMillis() - startTime) + "ms");

		startTime = System.currentTimeMillis();
		for (int i = 0; i < linkedList.size(); i++) {
			linkedList.get(i);
		}
		System.out.println("LinkedList for遍历速度："
				+ (System.currentTimeMillis() - startTime) + "ms");
		startTime = System.currentTimeMillis();
		for (Integer integer : linkedList) {
		}
		System.out.println("LinkedList foreach遍历速度："
				+ (System.currentTimeMillis() - startTime) + "ms");
	}
}
