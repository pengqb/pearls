package com.vela.iot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AResource implements IResource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AResource.class);

	@Override
	public String action(Request request) {
		try{
			logRequest(request);
			PubParamVerifier pubParamVerifier = new PubParamVerifier();
			pubParamVerifier.verify(request.getParams());
			
			
			
			execute(request);
		}catch(Exception400 e){
			return "error";
		}
		return null;
	}

	private void logRequest(Request request) {
		LOGGER.info("uri={},content={}", request.getUri(), request.getParams());
	}
}
