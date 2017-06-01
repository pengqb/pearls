package com.vela.iot.common;


public interface IResource {
	public String action(Request request);
	
	default public String execute(Request request) {
		String out = "";
		return out;
	}

}
