package com.vela.iot.active.netty.coap;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.LongAdder;

public class MetricHandler implements Runnable {

	@Override
	public void run() {
		LongAdder count = new LongAdder();
		long start = 0L;
		try{
			while(true){
				ContextMsg cm= CoAPServer.bq.take();
				String str = cm.getMsg().content().toString(CharsetUtil.UTF_8);
				//System.out.println(str);
				if ("start".equals(str)) {
					System.out.println("开始请求");
					start = System.nanoTime();
					count.reset();
				}
				count.increment();
				cm.getCtx().writeAndFlush(
						new DatagramPacket(Unpooled.copiedBuffer("nihao",
								CharsetUtil.UTF_8), cm.getMsg().sender())).sync();
				if ("end".equals(str)) {
					System.out.format("处理请求数%d,处理时间%d", count.longValue(),
							(System.nanoTime() - start));
					System.out.println("请求结束");
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
