package com.vela.iot.active.netty.coap;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeJavaClient implements Runnable {
	private static  CountDownLatch countDownLatch;
	private static int REQ_NUM;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NativeJavaClient.class);
	private static byte[] sendBuf = "hello"
			.getBytes();
//	private static byte[] sendBuf = "devSn=wZud4fM6SUuvvvBoFyGNYw&devKey=8I8LLGb7QaOZw6wgYInDrQ&devInfo=1123899"
//			.getBytes();
	private static String SERVER_HOST = "192.168.3.234";
	private static int SERVER_PORT = 5683;
	private static final NativeJavaClient nc = new NativeJavaClient();

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if (args.length < 3) {
			System.err.println("Usage: wordcount <serverhost> <threadNum> <reqNum>");
			System.exit(2);
		}
		NativeJavaClient.SERVER_HOST = args[0];
		Integer threadNum = Integer.valueOf(args[1]);
		countDownLatch = new CountDownLatch(threadNum);
		REQ_NUM = Integer.valueOf(args[2]);
		
		long start = System.nanoTime();
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			executorService.execute(nc);
		}
		countDownLatch.await();
		LOGGER.info("总请求数:{}*{},消费时间:{}纳秒", REQ_NUM,threadNum, System.nanoTime() - start);
		executorService.shutdown();
		
	}

	private void bio() throws IOException {
		DatagramSocket client = new DatagramSocket();
		InetAddress addr = InetAddress.getByName(SERVER_HOST);
		DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
				addr, SERVER_PORT);
		long start = System.nanoTime();
		for (int i = 0; i < REQ_NUM; i++) {

			client.send(sendPacket);
			byte[] recvBuf = new byte[100];
			DatagramPacket recvPacket = new DatagramPacket(recvBuf,
					recvBuf.length);
			client.receive(recvPacket);
			String recvStr = new String(recvPacket.getData(), 0,
					recvPacket.getLength());
			System.out.println("收到:" + recvStr);
		}
		LOGGER.info("总请求数:{},消费时间:{}纳秒", REQ_NUM, System.nanoTime() - start);
		client.close();
	}

	private void nio() throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		ServerSocket serverSocket = new ServerSocket(0); // 读取空闲的可用端口
		int port = serverSocket.getLocalPort();
		channel.socket().bind(new InetSocketAddress(port));
		long start = System.nanoTime();
		ByteBuffer buf = ByteBuffer.allocate(100);
		for (int i = 0; i < REQ_NUM; i++) {
			buf.clear();
			buf.put(sendBuf);
			buf.flip();
			int bytesSent = channel.send(buf, new InetSocketAddress(
					SERVER_HOST, SERVER_PORT));

			buf.clear();
			channel.receive(buf);
			String recvStr = new String(buf.array(), 0, buf.array().length);
			//System.out.println("收到:" + recvStr);
		}
		LOGGER.info("port:{},总请求数:{},消费时间:{}纳秒", port,REQ_NUM, System.nanoTime() - start);
		channel.close();
		serverSocket.close();
	}

	@Override
	public void run() {
		try {
			nio();
			countDownLatch.countDown();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
