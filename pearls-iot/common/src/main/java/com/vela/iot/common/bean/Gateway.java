package com.vela.iot.common.bean;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import static com.vela.iot.common.util.StringUtils.*;

public enum Gateway {
	// perf "String"比String.class性能提供1倍
	sn("String"), pwd("String"), slt("String"), key("String"), scrt("String"), acKey(
			"String"), acScrt("String"), active("Boolean"), activeTime("Long"), resetNum(
			"Integer"), activated("Boolean"), ability("Integer"), deadline(
			"Long");

	final String cls;

	Gateway(String cls) {
		this.cls = cls;
	}

	public final String getCls() {
		return cls;
	}

	public static final Map<String, String> enumToHash(
			Map<Gateway, Object> enumMap) {
		Map<String, String> hashMap = new HashMap<>();
		// perf lambda写法性能很差
		// enumMap.forEach((k,v) -> hashMap.put(k.name(), v.toString()));
		for (Entry<Gateway, Object> entry : enumMap.entrySet()) {
			hashMap.put(entry.getKey().name(), entry.getValue().toString());
		}
		return hashMap;
	}

	public static final Map<Gateway, Object> hashToEnum(
			Map<String, String> hashMap) {
		Map<Gateway, Object> enumMap = new EnumMap<Gateway, Object>(
				Gateway.class);
		for (Entry<String, String> entry : hashMap.entrySet()) {
			Gateway gw = Gateway.valueOf(entry.getKey());
			enumMap.put(gw, strToObj(entry.getValue(), gw.getCls()));
		}
		return enumMap;
	}

}

// public class Gateway {
// private String sn;
// private String pwd;
// private String key;
// private String scrt;
// private String acKey;
// private String acScrt;
// private boolean active;
// private long activeTime;
// private int resetNum;
// private boolean activated;
// private int ability;
// private long deadline;
// public String getSn() {
// return sn;
// }
// public void setSn(String sn) {
// this.sn = sn;
// }
// public String getPwd() {
// return pwd;
// }
// public void setPwd(String pwd) {
// this.pwd = pwd;
// }
// public String getKey() {
// return key;
// }
// public void setKey(String key) {
// this.key = key;
// }
// public String getScrt() {
// return scrt;
// }
// public void setScrt(String scrt) {
// this.scrt = scrt;
// }
// public String getAcKey() {
// return acKey;
// }
// public void setAcKey(String acKey) {
// this.acKey = acKey;
// }
// public String getAcScrt() {
// return acScrt;
// }
// public void setAcScrt(String acScrt) {
// this.acScrt = acScrt;
// }
//
// public long getActiveTime() {
// return activeTime;
// }
// public void setActiveTime(long activeTime) {
// this.activeTime = activeTime;
// }
// public int getResetNum() {
// return resetNum;
// }
// public void setResetNum(int resetNum) {
// this.resetNum = resetNum;
// }
//
// public boolean isActive() {
// return active;
// }
// public void setActive(boolean active) {
// this.active = active;
// }
// public boolean isActivated() {
// return activated;
// }
// public void setActivated(boolean activated) {
// this.activated = activated;
// }
// public int getAbility() {
// return ability;
// }
// public void setAbility(int ability) {
// this.ability = ability;
// }
// public long getDeadline() {
// return deadline;
// }
// public void setDeadline(long deadline) {
// this.deadline = deadline;
// }
// }
