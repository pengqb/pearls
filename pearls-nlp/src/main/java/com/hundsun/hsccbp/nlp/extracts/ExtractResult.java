package com.hundsun.hsccbp.nlp.extracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtractResult {
	private String code;

	private String destPath;

	public ExtractResult(String code, String destPath) {
		this.code = code;
		this.destPath = destPath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	@JsonProperty
	public String getDestPath() {
		return destPath;
	}
}
