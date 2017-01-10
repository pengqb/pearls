package com.vela.iot.active.netty.coap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

public class ContextMsg {
	ChannelHandlerContext ctx;
	DatagramPacket msg;

	public ContextMsg(ChannelHandlerContext ctx, DatagramPacket msg) {
		this.ctx = ctx;
		this.msg = msg;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public DatagramPacket getMsg() {
		return msg;
	}

	public void setMsg(DatagramPacket msg) {
		this.msg = msg;
	}

}
