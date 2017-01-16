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

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static int REQ_NUM = 10000;

	public static void main(String[] args) throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_RCVBUF, 65535)
					.option(ChannelOption.SO_SNDBUF, 65535)
					.handler(new CoAPClientHandler());
			Channel ch = b.bind(0).sync().channel();
			InetSocketAddress add = new InetSocketAddress("192.168.18.138", PORT);
			long start = System.nanoTime();
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("start",
					CharsetUtil.UTF_8), add));			
			for (int i = 0; i < REQ_NUM; i++) {
				ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
						"active?devSn=wZud4fM6SUuvvvBoFyGNYw&devKey=8I8LLGb7QaOZw6wgYInDrQ&devInfo="
								+ i, CharsetUtil.UTF_8), add));
				Thread.sleep(0, 100);
			}
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("end",
					CharsetUtil.UTF_8), add));
			if (!ch.closeFuture().await(5000)) {
				System.err.println("QOTM request timed out.");
			}
			LOGGER.info("总请求数:{},消费时间:{}纳秒", REQ_NUM, System.nanoTime() - start);
			
		} finally {
			group.shutdownGracefully();
		}
	}
}
