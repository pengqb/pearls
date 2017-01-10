package com.vela.iot.active.coap;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

public class GETHandler implements CoapHandler {
	private AtomicInteger sucCount;
	private AtomicInteger errCount;
	public GETHandler(AtomicInteger sucCount,AtomicInteger errCount){
		this.sucCount = sucCount;
		this.errCount = errCount;
	}

	@Override
	public void onLoad(CoapResponse response) {
		if (response!=null) {
			sucCount.incrementAndGet();
		} else {
			System.out.println("No response received.");
			errCount.incrementAndGet();
		}
		
	}

	@Override
	public void onError() {
		errCount.incrementAndGet();
		System.out.println("response error");
	}

}
