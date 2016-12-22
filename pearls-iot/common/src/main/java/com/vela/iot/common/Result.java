package com.vela.iot.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
	private String code;
	private String msg;
	
	public Result(){
		this.code = "suc";
		this.msg = "";
	}
	
	public Result(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	@JsonProperty
	public String getCode() {
		return code;
	}
	
	@JsonProperty
	public void setCode(String code) {
		this.code = code;
	}
	@JsonProperty
	public String getMsg() {
		return msg;
	}
	@JsonProperty
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
