package com.vela.iot.active.netty.coap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoblockJavaClient{
	public static CountDownLatch senderDownLatch;
	public static CountDownLatch receiverDownLatch;
	public static int REQ_NUM;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NoblockJavaClient.class);
	public static byte[] sendByte = "hello"
			.getBytes();
//	public static byte[] sendBuf = "devSn=wZud4fM6SUuvvvBoFyGNYw&devKey=8I8LLGb7QaOZw6wgYInDrQ&devInfo=1123899"
//			.getBytes();
	public static String SERVER_HOST = "192.168.1.105";
	public static int SERVER_PORT = 5683;
	private static final Receiver receiver = new Receiver();
	private static final Sender sender = new Sender();

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if (args.length < 3) {
			System.err.println("Usage: wordcount <serverhost> <threadNum> <reqNum>");
			System.exit(2);
		}
		NoblockJavaClient.SERVER_HOST = args[0];
		Integer threadNum = Integer.valueOf(args[1]);
		receiverDownLatch = new CountDownLatch(threadNum);
		senderDownLatch = new CountDownLatch(threadNum);
		REQ_NUM = Integer.valueOf(args[2]);		
		
		long start = System.nanoTime();
		ExecutorService sendService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			sendService.execute(sender);
		}
		senderDownLatch.await();
		
		ExecutorService receiverService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			receiverService.execute(receiver);
		}
		receiverDownLatch.await();
		
		
		sendService.shutdown();
		receiverService.shutdown();
	}
}

class Sender implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Sender.class);
	@Override
	public void run() {
		try {
			NoblockJavaClient.senderDownLatch.countDown();	
			service();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void service() throws IOException {
		DatagramChannel sendChannel = DatagramChannel.open();
		sendChannel.configureBlocking(false);
		long start = System.nanoTime();
		ByteBuffer sendBuf = ByteBuffer.allocate(1024);
		for (int i = 0; i < NoblockJavaClient.REQ_NUM; i++) {
			long startTime = System.nanoTime();
			sendBuf.clear();
			sendBuf.put(NoblockJavaClient.sendByte);
			sendBuf.flip();
			long bufTime = System.nanoTime();
			int bytesSent = sendChannel.send(sendBuf, new InetSocketAddress(
					NoblockJavaClient.SERVER_HOST, NoblockJavaClient.SERVER_PORT));
			long sendTime = System.nanoTime();
//			System.out.printf(
//					"第%d次调用,bufTime%d,sendTime%d\n", i,
//					bufTime - startTime, sendTime - bufTime);
		}
		//LOGGER.info("总请求数:{},消费时间:{}纳秒", NoblockJavaClient.REQ_NUM, System.nanoTime() - start);
		System.out.printf(
				"总请求数:%d,消费时间:%d纳秒\n", NoblockJavaClient.REQ_NUM, System.nanoTime() - start);
		//sendChannel.close();
	}
}

class Receiver implements Runnable {
	@Override
	public void run() {
		try {
			NoblockJavaClient.receiverDownLatch.countDown();
			service();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void service() throws IOException {
		DatagramChannel receiveChannel = DatagramChannel.open();
		receiveChannel.configureBlocking(false);
		ServerSocket serverSocket = new ServerSocket(0); // 读取空闲的可用端口
		int port = serverSocket.getLocalPort();
		//receiveChannel.socket().bind(new InetSocketAddress(port));
		receiveChannel.connect(new InetSocketAddress(NoblockJavaClient.SERVER_HOST, NoblockJavaClient.SERVER_PORT));// 连接服务端
		Selector selector = Selector.open();
		//receiveChannel.write(ByteBuffer.wrap(new String("客户端请求获取消息").getBytes()));
		receiveChannel.register(selector, SelectionKey.OP_READ);
		
		/** 外循环，已经发生了SelectionKey数目 */
		while (selector.select() > 0) {
			/* 得到已经被捕获了的SelectionKey的集合 */
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = null;
				try {
					key = iterator.next();
					iterator.remove();
					if (key.isReadable()) {
						reveice(key);
					}
					if (key.isWritable()) {
						// send(key);
					}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						if (key != null) {
							key.cancel();
							key.channel().close();
						}
					} catch (ClosedChannelException cex) {
						e.printStackTrace();
					}
				}
			}
		}
		
		//LOGGER.info("port:{},总请求数:{},消费时间:{}纳秒", port,REQ_NUM, System.nanoTime() - start);
		receiveChannel.close();
		serverSocket.close();
	}
	
	/* 接收 */
	synchronized public void reveice(SelectionKey key) throws IOException {
		String threadName = Thread.currentThread().getName();
		if (key == null)
			return;
		try {
			// ***用channel.receive()获取消息***//
			// ：接收时需要考虑字节长度
			DatagramChannel receiveChannel = (DatagramChannel) key.channel();
			ByteBuffer receiveBuf = ByteBuffer.allocate(1024);
			
			long startTime = System.nanoTime();
			receiveBuf.clear();
			receiveChannel.receive(receiveBuf);
			receiveBuf.flip();
			long recieveTime = System.nanoTime();
			String recvStr = new String(receiveBuf.array(), 0, receiveBuf.array().length);
			long endTime = System.nanoTime();
			System.out.println("收到:" + recvStr);
			System.out.printf(
					"该次调用,recieveTime%d,endTime%d\n",recieveTime - startTime, endTime - recieveTime);

		} catch (PortUnreachableException ex) {
			System.out.println(threadName + "服务端端口未找到!");
		}
	}
}
