package com.hundsun.hsccbp.digraph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static String initGraphStrPattern = "([A-E]{2}[1-9]{1},{1})*([A-E]{2}[1-9]{1}){1}";
	private static String routeStrPattern = "[A-E]{1}(\\-[A-E])+";
	public static String initGraphStrError = "有向图初始化输入需要按以下格式：城镇以字母表的前几个字符A-E命名，两个城镇(A到B)之间的路线距离为5，表示为AB5";
	public static String routeStrError = "指定路径参考格式：A-C-E-A";
	
	public static boolean initGraphStrMatchs(String initGraphStr) {
		return Pattern.matches(initGraphStrPattern, initGraphStr);
	}
	
	public static boolean routeStrMatchs(String routeStr) {
		return Pattern.matches(routeStrPattern, routeStr);
	}
	
	/**
	 * 字符串循环检测，如果字符串里重复出现相同的子字符串，则表示出现了循环。用字符串表示的路径，如果出现了循环，则肯定不是最短路径。
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return true:字符串有循环; false:字符串没循环
	 */
	public static boolean circleCheck(String str) {
		boolean flag = false;
		//String matcherStr = "";
		for (int i = 0; i < str.length(); i++) {
			for (int j = i + 1; j < str.length(); j++) {
				String temp = str.substring(i, j);
				temp = temp + temp;
				Matcher m = Pattern.compile(temp).matcher(str);
				if (m.find()) {
					//matcherStr = temp;
					flag = true;
					break;
				}
			}
		}
//		if (flag) {
//			System.out.println("循环字符串:" + matcherStr);
//		} else {
//			System.out.println("没有循环字符串");
//		}
		return flag;
	}
}
