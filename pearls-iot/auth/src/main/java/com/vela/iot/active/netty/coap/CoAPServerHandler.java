package com.vela.iot.active.netty.coap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.active.netty.http.ActiveResource;

public class CoAPServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoAPServerHandler.class);

	LongAdder count = new LongAdder();
	LongAdder totalTime = new LongAdder();

	ChannelFutureListener listener = new ChannelFutureListener() {
		public void operationComplete(ChannelFuture future) {
			// do nothing
		}
	};

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		count.increment();
		String req = msg.content().toString(CharsetUtil.UTF_8);
		long startTime = System.nanoTime();
		ActiveResource ar = new ActiveResource();
		ar.activeAction(req);
		//LOGGER.debug(req);

		ByteBuf byteBuf = Unpooled.copiedBuffer(
				"{\"pt\":\"fZud4fM6SUuvvvBoFyGNYw\",\"count\":" + count + "}",
				CharsetUtil.UTF_8);

		//long bytebufTime = System.nanoTime();
		DatagramPacket dp = new DatagramPacket(byteBuf, msg.sender());
		//long dpTime = System.nanoTime();

		ctx.write(dp).addListener(listener);
		//write和writeAndFlush的区别
		//write的处理流程：把message放入到buffer中，所以不阻塞，目的是为了将其发送到目的socket的内核缓冲区中。
		//至于什么时候发送（当然是对应的socket发送write可写事件的时候）呢? 
		//writeAndFlush：把message放入到buffer中再触发socket的write。所以会网络阻塞
		//ctx.pipeline()pipeline的性能是否还会提升
		
		//long writeTime = System.nanoTime();

		// CoAPServer.bq.offer(req, 10, TimeUnit.MILLISECONDS);
		long incrementTime = System.nanoTime() - startTime;
		totalTime.add(incrementTime);

//		System.out.printf("第%d次调用,bytebufTime%d,dpTime%d,writeTime%d\n",
//				count.longValue(), bytebufTime - startTime, dpTime
//						- bytebufTime, writeTime - dpTime);

		if (count.longValue() % 10000 == 0) {
//			LOGGER.debug("第{}次调用,累计耗时{}", count.longValue(), totalTime);
			System.out.printf("第%d次调用,累计耗时%d\n", count.longValue(),
					totalTime.longValue());
			totalTime.reset();
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}

}
