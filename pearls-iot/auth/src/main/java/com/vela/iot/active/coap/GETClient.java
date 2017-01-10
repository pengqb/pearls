package com.vela.iot.active.coap;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GETClient {
	private static Logger LOGGER = LoggerFactory.getLogger(GETClient.class);
	private static int REQ_NUM = 10000;

	public static void main(String args[]) {

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
			for (int i = 0; i < REQ_NUM; i++) {
				CoapResponse response = client.get();

				if (response != null) {
					// access advanced API with access to more details through
					// .advanced()
					System.out.println(Utils.prettyPrint(response));
				} else {
					LOGGER.error("No response received.");
				}
			}
			LOGGER.info("总请求数:{},消费时间:{}纳秒", REQ_NUM,System.nanoTime() - start);
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
