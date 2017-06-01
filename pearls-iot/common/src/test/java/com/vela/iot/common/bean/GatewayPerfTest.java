package com.vela.iot.common.bean;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vela.iot.common.PerfTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GatewayPerfTest extends PerfTest {
	static final Map<String, String> srcHash = new HashMap<>();
	static final Map<Gateway, Object> srcEnum = new EnumMap<Gateway, Object>(
			Gateway.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		srcHash.put("sn", "vstb002260cszgf");
		srcHash.put("pwd", "vwcM6dzk");
		srcHash.put("key", "PyUE4E+JEdOaDAMF6CwzAQ");
		srcHash.put("scrt", "_IwGDV1ziqX7UWRZ");
		srcHash.put("active", "true");

		srcEnum.put(Gateway.sn, "vstb002260cszgf");
		srcEnum.put(Gateway.pwd, "vwcM6dzk");
		srcEnum.put(Gateway.key, "PyUE4E+JEdOaDAMF6CwzAQ");
		srcEnum.put(Gateway.scrt, "_IwGDV1ziqX7UWRZ");
		srcEnum.put(Gateway.active, true);
		Gateway.enumToHash(srcEnum);
		Gateway.hashToEnum(srcHash);
	}
	@Test
	public void aaaa() {
	}

	@Test(timeout = 150)
	public void testEnumToHash() {
		for (int i = 0; i < count; i++) {
			Gateway.enumToHash(srcEnum);
		}
	}

	@Test(timeout = 150)
	public void testHashToEnum() {
		for (int i = 0; i < count; i++) {
			 Gateway.hashToEnum(srcHash);
		}
	}
}
