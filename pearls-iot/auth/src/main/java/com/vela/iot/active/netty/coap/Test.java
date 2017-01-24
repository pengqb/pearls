package com.vela.iot.active.netty.coap;

import java.io.IOException;
import java.util.BitSet;

public class Test {
	public static void main(String[] args) throws IOException {
//		Integer binary = 0b11;
//		System.out.println(binary);
//		System.out.println(Integer.toBinaryString(binary));
		//从高位到低位分别表示:
		//第一位，是否打印异步日志；第二位是否使用jedis写redis；第三位使用使用Lettuce写redis；第四位是否使用mongodb
		String bin = "11000000";
		BitSet bits = new BitSet(8);
		for(int i=0;i< bin.length();i++){
			if(bin.charAt(i) == '1')
				bits.flip(i);
		}
		
		for(int i=0;i< bin.length();i++){
			System.out.println(bits.get(i));
		}
			
		

//		ServerSocket serverSocket = new ServerSocket(0); // 读取空闲的可用端口
//		int port = serverSocket.getLocalPort();
//		System.out.println("系统分配的端口号 port=" + port);
		
//		LongAdder count = new LongAdder();
//		count.add(45);
//		System.out.format("处理请求数%d,处理时间%d",count.longValue(),System.nanoTime() );
	}
}
