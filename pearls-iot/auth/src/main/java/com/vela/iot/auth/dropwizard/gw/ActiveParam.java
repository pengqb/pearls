package com.vela.iot.auth.dropwizard.gw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActiveParam {
	//private String devSn;
	private String devKey;
	public ActiveParam(){
		
	}
	
	public ActiveParam(String devKey){
	//	this.devSn = devSn;
		this.devKey = devKey;
	}

//	@JsonProperty
//	public String getDevSn() {
//		return devSn;
//	}
//
//	@JsonProperty
//	public void setDevSn(String devSn) {
//		this.devSn = devSn;
//	}

	@JsonProperty
	public String getDevKey() {
		return devKey;
	}

	@JsonProperty
	public void setDevKey(String devKey) {
		this.devKey = devKey;
	}
}
