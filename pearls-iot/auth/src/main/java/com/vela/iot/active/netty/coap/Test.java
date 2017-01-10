package com.vela.iot.active.netty.coap;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.LongAdder;

public class Test {
	public static void main(String[] args) throws IOException {
//		Integer binary = 0b11;
//		System.out.println(binary);
//		System.out.println(Integer.toBinaryString(binary));

//		ServerSocket serverSocket = new ServerSocket(0); // 读取空闲的可用端口
//		int port = serverSocket.getLocalPort();
//		System.out.println("系统分配的端口号 port=" + port);
		
		LongAdder count = new LongAdder();
		count.add(45);
		System.out.format("处理请求数%d,处理时间%d",count.longValue(),System.nanoTime() );
	}
}
