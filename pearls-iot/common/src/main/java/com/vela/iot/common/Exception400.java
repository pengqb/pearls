package com.vela.iot.common;

public class Exception400 extends RuntimeException {
	private String code;
	private String msg;
	
	public Exception400(String code,String msg){
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
