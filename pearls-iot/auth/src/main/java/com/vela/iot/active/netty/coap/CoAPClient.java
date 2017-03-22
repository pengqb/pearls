/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.vela.iot.active.netty.coap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.active.netty.http.ActiveResource;

/**
 * A UDP broadcast client that asks for a quote of the moment (QOTM) to
 * {@link QuoteOfTheMomentServer}.
 *
 * Inspired by <a href=
 * "http://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html"
 * >the official Java tutorial</a>.
 */
public final class CoAPClient {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoAPClient.class);
	static final int PORT = Integer
			.parseInt(System.getProperty("port", "5683"));
	private static int REQ_NUM = 1000000;

	public static void main(String[] args) throws Exception {
		int bossCount =1;
		if (args.length > 3) {
			bossCount = Integer.parseInt(args[3]);
		}
		int bufSize =655350;
		if (args.length > 4) {
			bufSize = Integer.parseInt(args[4]);
		}
		EventLoopGroup group = new NioEventLoopGroup(bossCount);
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_RCVBUF, bufSize)
					.option(ChannelOption.SO_SNDBUF, bufSize)
					.handler(new CoAPClientHandler());
			Channel ch = b.bind(0).sync().channel();
			String ip = "192.168.2.185";
			if (args.length > 0) {
				ip = args[0];
			}
			if (args.length > 1) {
				REQ_NUM = Integer.parseInt(args[1]);
			}
			int concNum = 10;
			if (args.length > 2) {
				concNum = Integer.parseInt(args[2]);
			}
			InetSocketAddress add = new InetSocketAddress(ip, PORT);

			long start = System.nanoTime();
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("start",
					CharsetUtil.UTF_8), add));
			for (int i = 0; i < REQ_NUM; i++) {
				ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
						"active?devSn=wZud4fM6SUuvvvBoFyGNYw&devKey=8I8LLGb7QaOZw6wgYInDrQ&devInfo="
								+ i, CharsetUtil.UTF_8), add));
				if (i % concNum == 0)
					Thread.sleep(0, 1);
			}
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("end",
					CharsetUtil.UTF_8), add));
			// LOGGER.info("总请求数:{},消费时间:{}纳秒", REQ_NUM, System.nanoTime() -
			// start);
			System.out.printf("总请求数:%d,消费时间:%d纳秒", REQ_NUM, System.nanoTime()
					- start);
			if (!ch.closeFuture().await(5000)) {
				System.err.println("QOTM request timed out.");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}

// class Sender implements Runnable {
// private static final Logger LOGGER = LoggerFactory
// .getLogger(Sender.class);
// @Override
// public void run() {
// try {
// NoblockJavaClient.senderDownLatch.countDown();
// service();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// private void service() throws IOException {
// DatagramChannel sendChannel = DatagramChannel.open();
// sendChannel.configureBlocking(false);
// long start = System.nanoTime();
// ByteBuffer sendBuf = ByteBuffer.allocate(1024);
// for (int i = 0; i < NoblockJavaClient.REQ_NUM; i++) {
// long startTime = System.nanoTime();
// sendBuf.clear();
// sendBuf.put(NoblockJavaClient.sendByte);
// sendBuf.flip();
// long bufTime = System.nanoTime();
// int bytesSent = sendChannel.send(sendBuf, new InetSocketAddress(
// NoblockJavaClient.SERVER_HOST, NoblockJavaClient.SERVER_PORT));
// long sendTime = System.nanoTime();
// // System.out.printf(
// // "第%d次调用,bufTime%d,sendTime%d\n", i,
// // bufTime - startTime, sendTime - bufTime);
// }
// //LOGGER.info("总请求数:{},消费时间:{}纳秒", NoblockJavaClient.REQ_NUM,
// System.nanoTime() - start);
// System.out.printf(
// "总请求数:%d,消费时间:%d纳秒\n", NoblockJavaClient.REQ_NUM, System.nanoTime() - start);
// //sendChannel.close();
// }
// }
