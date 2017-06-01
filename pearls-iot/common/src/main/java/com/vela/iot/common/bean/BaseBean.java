package com.vela.iot.common.bean;


public interface BaseBean{
	String cls = null;

	public default String getCls() {
		return cls;
	}
	
	public default Object strToObj(String str, String cls) {
		Object obj = null;
		//perf 最省时间的最先判断，提升性能
		if("String".equals(cls)){
			obj = str;
		} else if ("Boolean".equals(cls)) {
			obj = Boolean.valueOf(str);
		} else if ("Integer".equals(cls)) {
			obj = Integer.valueOf(str);
		} else if ("Long".equals(cls)) {
			obj = Long.valueOf(str);
		} 
		return obj;
	}

}
