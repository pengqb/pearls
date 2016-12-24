package com.vela.iot.auth.dropwizard.gw;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActiveParam {
	private String devSn;
	private String devKey;

	public ActiveParam() {

	}

	public ActiveParam(String devSn, String devKey) {
		this.devSn = devSn;
		this.devKey = devKey;
	}

	@JsonProperty
	public String getDevSn() {
		return devSn;
	}

	@JsonProperty
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	@JsonProperty
	public String getDevKey() {
		return devKey;
	}

	@JsonProperty
	public void setDevKey(String devKey) {
		this.devKey = devKey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ActiveParam)) {
			return false;
		}

		final ActiveParam that = (ActiveParam) o;

		return Objects.equals(this.devSn, that.devSn)
				&& Objects.equals(this.devKey, that.devKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(devSn, devKey);
	}
}
