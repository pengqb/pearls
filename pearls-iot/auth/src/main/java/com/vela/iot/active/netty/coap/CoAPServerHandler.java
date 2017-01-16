package com.vela.iot.active.netty.coap;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoAPServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CoAPServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		String req = msg.content().toString(CharsetUtil.UTF_8);
		//System.out.println(req);
		 ctx.writeAndFlush(new
				 DatagramPacket(Unpooled.copiedBuffer("{\"pt\":\"fZud4fM6SUuvvvBoFyGNYw\",\"at\":\"fI8LLGb7QaOZw6wgYInDrQ\"}",
				 CharsetUtil.UTF_8), msg.sender())).sync();
		
		CoAPServer.bq.offer(req,10,TimeUnit.MILLISECONDS);
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
