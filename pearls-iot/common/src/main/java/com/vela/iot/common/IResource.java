package com.vela.iot.common;

import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;

public interface IResource {
	public String action(Request request);
	
	default public String execute(Request request) {
		String out = "";
		return out;
	}

}
