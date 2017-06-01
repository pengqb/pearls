package com.vela.iot.common;

import java.util.Map;

public class Request {
	private String uri;
	private String method;
	private String version;
	private Map<String,String> headers;
	private Map<Param,Object> params;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Map<Param, Object> getParams() {
		return params;
	}
	public void setParams(Map<Param, Object> params) {
		this.params = params;
	}	
}
