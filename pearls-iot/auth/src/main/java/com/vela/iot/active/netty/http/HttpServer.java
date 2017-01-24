package com.vela.iot.active.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpServer.class);

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			String funcBitsStr = args[0];
			for(int i=0;i< funcBitsStr.length();i++){
				if(funcBitsStr.charAt(i) == '1')
					ActiveResource.bits.flip(i);
			}
		}
		ActiveConf conf = new ActiveConf();
		conf.initSystem();
		String host = "0.0.0.0";
		int port = 8080;
		int bossCount = 1;
		int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
		int executorThreadCount = Runtime.getRuntime().availableProcessors() * 4;
		
		//从高位到低位分别表示:
		//第一位，是否打印异步日志；第二位是否使用jedis写redis；第三位使用使用Lettuce写redis；第四位是否使用mongodb sync
		//第五位 是否使用mongodb sync bulk;第六位 是否使用mongodb async; 第七位 是否使用mongodb async bulk
		//第八位，是否使用influxdb，批量插入; 第九位，是否使用influxdb，udp插入; 第十位是否使用elasticsearch同步更新文档
		//第十一位是否使用elasticsearch异步更新文档；第十二位是否使用elasticsearch异步批量更新文档
		
//		if (args.length > 0) {
//			bossCount = Integer.parseInt(args[0]);
//		}
//
//		if (args.length > 1) {
//			ioWorkerCount = Integer.parseInt(args[1]);
//		}
//
//		if (args.length > 2) {
//			executorThreadCount = Integer.parseInt(args[2]);
//		}

		LOGGER.info("bossCount:{}", bossCount);
		LOGGER.info("ioWorkerCount:{}", ioWorkerCount);
		LOGGER.info("executorThreadCount:{}", executorThreadCount);

		EventLoopGroup bossGroup = new NioEventLoopGroup(bossCount);
		EventLoopGroup workerGroup = new NioEventLoopGroup(ioWorkerCount);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// .handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new HttpServerCodec());
							p.addLast(new HttpContentCompressor(1));
							p.addLast(new HttpServerHandler());
						}
					})
					// Netty默认不使用内存池，需要在创建客户端或者服务端的时候进行指定.
					.option(ChannelOption.ALLOCATOR,
							PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					// TCP_NODELAY就是用于启用或关闭Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
					// 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送(典型的应用就是文件服务器)。默认为false。
					.childOption(ChannelOption.TCP_NODELAY, true);
			Channel ch = b.bind(host, port).sync().channel();
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	
}
