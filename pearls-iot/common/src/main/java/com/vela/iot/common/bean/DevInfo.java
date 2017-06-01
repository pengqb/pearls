package com.vela.iot.common.bean;

import static com.vela.iot.common.util.StringUtils.strToObj;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum DevInfo {
	id("Integer"), devType("Integer"), commType("String"), commModel("String"), vendor(
			"String"), model("String"), verSw("String"), verHw("String"), desc(
			"String"), status("Integer"), applyNum("Integer"), bindNum(
			"Integer"), appId("String");

	final String cls;

	DevInfo(String cls) {
		this.cls = cls;
	}

	public final String getCls() {
		return cls;
	}
	
	public static final Map<String, String> enumToHash(Map<DevInfo, Object> enumMap) {
		Map<String, String> hashMap = new HashMap<>();
		//perf lambda写法性能很差
		//enumMap.forEach((k,v) -> hashMap.put(k.name(), v.toString()));
		for (Entry<DevInfo, Object> entry : enumMap.entrySet()) {
			hashMap.put(entry.getKey().name(), entry.getValue().toString());
		}
		return hashMap;
	}

	public static final Map<DevInfo, Object> hashToEnum(Map<String, String> hashMap) {
		Map<DevInfo, Object> enumMap = new EnumMap<DevInfo, Object>(
				DevInfo.class);
		for (Entry<String, String> entry : hashMap.entrySet()) {
			DevInfo gw = DevInfo.valueOf(entry.getKey());
			enumMap.put(gw,
					strToObj(entry.getValue(), gw.getCls()));
		}
		return enumMap;
	}
}
