package com.vela.iot.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
	/**
	 * 字符串转换成对应类型
	 * @param str 字符串
	 * @param cls 转换成对应的类型
	 * @return
	 */
	public static final Object strToObj(String str, String cls) {
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
