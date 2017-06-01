package com.vela.iot.common.bean;

public class App {

	private String key;

	private String secret;

	private Integer userId;

	private String enable;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "InfoAppKey [key=" + key + ", secret=" + secret
				+ ", userId=" + userId + ", enable=" + enable + "]";
	}
}