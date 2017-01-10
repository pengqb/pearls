package com.vela.iot.active.coap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.californium.core.CoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynGETClient {
	private static Logger LOGGER = LoggerFactory.getLogger(AsynGETClient.class);
	private static int REQ_NUM = 10000;

	public static void main(String args[]) {
		AtomicInteger sucCount = new AtomicInteger();
		AtomicInteger errCount = new AtomicInteger();
		URI uri = null; // URI parameter of the request

		if (args.length > 0) {

			// input URI from command line arguments
			try {
				uri = new URI(args[0]);
			} catch (URISyntaxException e) {
				LOGGER.error("Invalid URI: {}" + e.getMessage(), e);
				System.exit(-1);
			}

			CoapClient client = new CoapClient(uri);
			long start = System.nanoTime();
			GETHandler handler = new GETHandler(sucCount, errCount);
			for (int i = 0; i < REQ_NUM; i++) {
				client.get(handler);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LOGGER.info("总请求数:{},正常返回数:{},错误返回数:{},消费时间:{}纳秒", REQ_NUM,
					sucCount, errCount, System.nanoTime() - start);
		} else {
			// display help
			LOGGER.error("Californium (Cf) GET Client");
			LOGGER.error("(c) 2014, Institute for Pervasive Computing, ETH Zurich");
			LOGGER.error("");
			LOGGER.error("Usage: " + GETClient.class.getSimpleName() + " URI");
			LOGGER.error("  URI: The CoAP URI of the remote resource to GET");
		}
	}

}
