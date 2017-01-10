package com.vela.iot.active.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveResource extends CoapResource {
	private static Logger LOGGER = LoggerFactory.getLogger(ActiveResource.class);
	public ActiveResource() {

		// set resource identifier
		super("active");

		// set display name
		getAttributes().setTitle("Active Resource");
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		LOGGER.info("coaprequest:\n{}", Utils.prettyPrint(exchange.advanced().getRequest()));
		// respond to the request
		exchange.respond("{\"pt\":\"fZud4fM6SUuvvvBoFyGNYw\",\"at\":\"fI8LLGb7QaOZw6wgYInDrQ\"}");
	}

	@Override
	public void handlePOST(CoapExchange exchange) {
		handleGET(exchange);
	}
}
