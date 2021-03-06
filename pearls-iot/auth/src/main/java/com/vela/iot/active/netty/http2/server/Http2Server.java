/*
 * Copyright 2014 The Netty Project
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

package com.vela.iot.active.netty.http2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http2.Http2Codec;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectedListenerFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * A HTTP/2 Server that responds to requests with a Hello World. Once started,
 * you can test the server with the example client.
 */
public final class Http2Server {

	//static final boolean SSL = System.getProperty("ssl") != null;
	static final boolean SSL = false;

	static final int PORT = Integer.parseInt(System.getProperty("port",
			SSL ? "8443" : "8080"));
	static final String HOST = "0.0.0.0";
	

	public static void main(String[] args) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SslProvider provider = OpenSsl.isAlpnSupported() ? SslProvider.OPENSSL
					: SslProvider.JDK;
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder
					.forServer(ssc.certificate(), ssc.privateKey())
					.sslProvider(provider)
					/*
					 * NOTE: the cipher filter may not include all ciphers
					 * required by the HTTP/2 specification. Please refer to the
					 * HTTP/2 specification for cipher requirements.
					 */
					.ciphers(Http2SecurityUtil.CIPHERS,
							SupportedCipherSuiteFilter.INSTANCE)
					.applicationProtocolConfig(
							new ApplicationProtocolConfig(
									Protocol.ALPN,
									// NO_ADVERTISE is currently the only mode
									// supported by both OpenSsl and JDK
									// providers.
									SelectorFailureBehavior.NO_ADVERTISE,
									// ACCEPT is currently the only mode
									// supported by both OpenSsl and JDK
									// providers.
									SelectedListenerFailureBehavior.ACCEPT,
									ApplicationProtocolNames.HTTP_2,
									ApplicationProtocolNames.HTTP_1_1)).build();
		} else {
			sslCtx = null;
		}
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(7);
		try {
			LastInboundHandler serverLastInboundHandler = new SharableLastInboundHandler();
			ServerBootstrap b = new ServerBootstrap();
			// BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new Http2Codec(true,serverLastInboundHandler));
							//p.addLast(new HttpContentCompressor(1));
							p.addLast(new HelloWorldHttp2HandlerBuilder().build());
						}
					});

			Channel ch = b.bind(HOST,PORT).sync().channel();

			System.err
					.println("Open your HTTP/2-enabled web browser and navigate to "
							+ (SSL ? "https" : "http")
							+ "://127.0.0.1:"
							+ PORT
							+ '/');

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	@Sharable
    private static class SharableLastInboundHandler extends LastInboundHandler {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelInactive();
        }
    }
}
