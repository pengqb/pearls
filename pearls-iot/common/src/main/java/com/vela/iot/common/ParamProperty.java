package com.vela.iot.common;

public class ParamProperty {
	private int minLength;
	private int maxLength;
	private boolean optional;
	
	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public ParamProperty(int minLength,int maxLength,boolean optional){
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.optional = optional;
	}
	
	
}
