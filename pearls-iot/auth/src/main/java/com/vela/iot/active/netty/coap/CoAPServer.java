package com.vela.iot.active.netty.coap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.active.netty.http.ActiveResource;

public class CoAPServer {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoAPServer.class);
	public static BlockingQueue<String> bq= new ArrayBlockingQueue<>(10240);
	
	public static void main(String[] args) {
		if (args.length > 0) {
			String funcBitsStr = args[0];
			for(int i=0;i< funcBitsStr.length();i++){
				if(funcBitsStr.charAt(i) == '1')
					ActiveResource.bits.flip(i);
			}
		}
		String host = "0.0.0.0";
		int port = NetworkConfig.getStandard().getInt(
				NetworkConfig.Keys.COAP_PORT);
		int bossCount = 1;
		int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
		int executorThreadCount = Runtime.getRuntime().availableProcessors() * 4;

		LOGGER.info("bossCount:{}", bossCount);
		LOGGER.info("ioWorkerCount:{}", ioWorkerCount);
		LOGGER.info("executorThreadCount:{}", executorThreadCount);

		EventLoopGroup group = new NioEventLoopGroup(bossCount);

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST, true)
					.option(ChannelOption.SO_RCVBUF, 65535)
					.option(ChannelOption.SO_SNDBUF, 65535)
					.handler(new CoAPServerHandler());
			
			//ExecutorService executorService = Executors.newFixedThreadPool(1);
			//executorService.execute(new MetricHandler());
			
			b.bind(port).sync().channel().closeFuture().await();
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			group.shutdownGracefully();
		}
	}

}
