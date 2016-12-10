package com.hundsun.hsccbp.hadoop;

public class Line implements Comparable<Line> {

	private String key;
	private Integer value;

	public Line(String key, Integer value) {
		this.key = key;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int compareTo(Line o) {
		return value.compareTo(o.getValue());
	}
	
}
