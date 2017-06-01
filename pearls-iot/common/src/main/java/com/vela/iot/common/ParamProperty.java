package com.vela.iot.common;

enum Type{
	b,i,l,s
}

public class ParamProperty {
	private Type type;
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
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ParamProperty(Type type,int minLength,int maxLength,boolean optional){
		this.type = type;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.optional = optional;
	}
	
	
}
