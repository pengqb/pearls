package com.vela.iot.auth.dropwizard.gw;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vela.iot.common.Result;

public class ActiveResult extends Result {
	private String primaryToken;
	private String accessToken;
	private List<String> urls = new ArrayList<>();

	@JsonProperty("pt")
	public String getPrimaryToken() {
		return primaryToken;
	}

	@JsonProperty("pt")
	public void setPrimaryToken(String primaryToken) {
		this.primaryToken = primaryToken;
	}

	@JsonProperty("at")
	public String getAccessToken() {
		return accessToken;
	}

	@JsonProperty("at")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty
	public List<String> getUrls() {
		return urls;
	}

	@JsonProperty
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
}
