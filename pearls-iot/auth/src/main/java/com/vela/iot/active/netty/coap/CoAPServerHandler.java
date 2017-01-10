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
		// ByteBuf bb = msg.content();
		// int i = bb.readInt();
		// System.out.println("i:"+i);
		// String binary = Integer.toBinaryString(i);
		// System.out.println("binary:"+binary);
		// CharSequence cs = bb.readCharSequence(4,CharsetUtil.UTF_8);
		// cs.toString().getBytes();
		// DataParser parser = new DataParser(cs.toString().getBytes());
		// System.out.println(parser);
		String req = msg.content().toString(CharsetUtil.UTF_8);
		//System.out.println(req);
//		ctx.writeAndFlush(
//				new DatagramPacket(Unpooled.copiedBuffer("nihao",
//						CharsetUtil.UTF_8), msg.sender())).sync();
		
		ContextMsg cm = new ContextMsg(ctx,msg);
		CoAPServer.bq.offer(cm,10,TimeUnit.MILLISECONDS);
		
		// ctx.writeAndFlush(new
		// DatagramPacket(Unpooled.copiedBuffer("{\"pt\":\"fZud4fM6SUuvvvBoFyGNYw\",\"at\":\"fI8LLGb7QaOZw6wgYInDrQ\"}",
		// CharsetUtil.UTF_8), msg.sender())).sync();
		
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

	// private void receiveMessage(DatagramPacket msg) {
	// ByteBuf bb = msg.content();
	// CharSequence cs = bb.readCharSequence(4,CharsetUtil.UTF_8);
	// cs.toString().getBytes();
	// DataParser parser = new DataParser(cs.toString().getBytes());
	//
	// if (parser.isRequest()) {
	// // This is a request
	// Request request;
	// try {
	// request = parser.parseRequest();
	// } catch (IllegalStateException e) {
	// StringBuffer log = new StringBuffer("message format error caused by ")
	// .append(raw.getInetSocketAddress());
	// if (!parser.isReply()) {
	// // manually build RST from raw information
	// EmptyMessage rst = new EmptyMessage(Type.RST);
	// rst.setMID(parser.getMID());
	// rst.setToken(new byte[0]);
	// rst.setDestination(raw.getAddress());
	// rst.setDestinationPort(raw.getPort());
	// for (MessageInterceptor interceptor:interceptors)
	// interceptor.sendEmptyMessage(rst);
	// connector.send(serializer.serialize(rst));
	// log.append(" and reset");
	// }
	// if (LOGGER.isLoggable(Level.INFO)) {
	// LOGGER.info(log.toString());
	// }
	// return;
	// }
	// request.setSource(raw.getAddress());
	// request.setSourcePort(raw.getPort());
	// request.setSenderIdentity(raw.getSenderIdentity());
	//
	// /*
	// * Logging here causes significant performance loss.
	// * If necessary, add an interceptor that logs the messages,
	// * e.g., the MessageTracer.
	// */
	//
	// for (MessageInterceptor interceptor:interceptors)
	// interceptor.receiveRequest(request);
	//
	// // MessageInterceptor might have canceled
	// if (!request.isCanceled()) {
	// Exchange exchange = matcher.receiveRequest(request);
	// if (exchange != null) {
	// exchange.setEndpoint(CoapEndpoint.this);
	// coapstack.receiveRequest(exchange, request);
	// }
	// }
	//
	// } else if (parser.isResponse()) {
	// // This is a response
	// Response response = parser.parseResponse();
	// response.setSource(raw.getAddress());
	// response.setSourcePort(raw.getPort());
	//
	// /*
	// * Logging here causes significant performance loss.
	// * If necessary, add an interceptor that logs the messages,
	// * e.g., the MessageTracer.
	// */
	//
	// for (MessageInterceptor interceptor:interceptors)
	// interceptor.receiveResponse(response);
	//
	// // MessageInterceptor might have canceled
	// if (!response.isCanceled()) {
	// Exchange exchange = matcher.receiveResponse(response);
	// if (exchange != null) {
	// exchange.setEndpoint(CoapEndpoint.this);
	// response.setRTT(System.currentTimeMillis() - exchange.getTimestamp());
	// coapstack.receiveResponse(exchange, response);
	// } else if (response.getType() != Type.ACK) {
	// LOGGER.fine("Rejecting unmatchable response from " +
	// raw.getInetSocketAddress());
	// reject(response);
	// }
	// }
	//
	// } else if (parser.isEmpty()) {
	// // This is an empty message
	// EmptyMessage message = parser.parseEmptyMessage();
	// message.setSource(raw.getAddress());
	// message.setSourcePort(raw.getPort());
	//
	// /*
	// * Logging here causes significant performance loss.
	// * If necessary, add an interceptor that logs the messages,
	// * e.g., the MessageTracer.
	// */
	//
	// for (MessageInterceptor interceptor:interceptors)
	// interceptor.receiveEmptyMessage(message);
	//
	// // MessageInterceptor might have canceled
	// if (!message.isCanceled()) {
	// // CoAP Ping
	// if (message.getType() == Type.CON || message.getType() == Type.NON) {
	// LOGGER.info("Responding to ping by " + raw.getInetSocketAddress());
	// reject(message);
	// } else {
	// Exchange exchange = matcher.receiveEmptyMessage(message);
	// if (exchange != null) {
	// exchange.setEndpoint(CoapEndpoint.this);
	// coapstack.receiveEmptyMessage(exchange, message);
	// }
	// }
	// }
	// } else {
	// LOGGER.finest("Silently ignoring non-CoAP message from " +
	// raw.getInetSocketAddress());
	// }
	// }
}
