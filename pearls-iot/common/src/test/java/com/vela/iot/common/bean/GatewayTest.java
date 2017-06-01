package com.vela.iot.common.bean;

import static org.junit.Assert.assertEquals;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vela.iot.common.bean.Gateway;

public class GatewayTest {
	static Map<String, String> srcHash = new HashMap<>();
	static Map<Gateway, Object> srcEnum = new EnumMap<Gateway, Object>(
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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEnumToHash() {
		Map<String, String> dstMap = Gateway.enumToHash(srcEnum);
		assertEquals("源和目标map不相等", srcHash.get("sn"), dstMap.get("sn"));
		assertEquals("源和目标map不相等", srcHash.get("pwd"), dstMap.get("pwd"));
		assertEquals("源和目标map不相等", srcHash.get("key"), dstMap.get("key"));
		assertEquals("源和目标map不相等", srcHash.get("scrt"), dstMap.get("scrt"));
		assertEquals("源和目标map不相等", srcHash.get("active"), dstMap.get("active"));
	}

	@Test
	public void testHashToEnum() {
		Map<Gateway, Object> dstEnum = Gateway.hashToEnum(srcHash);
		assertEquals("源和目标gw不相等", srcEnum.get(Gateway.sn), dstEnum.get(Gateway.sn));
		assertEquals("源和目标gw不相等", srcEnum.get(Gateway.pwd), dstEnum.get(Gateway.pwd));
		assertEquals("源和目标gw不相等", srcEnum.get(Gateway.key), dstEnum.get(Gateway.key));
		assertEquals("源和目标gw不相等", srcEnum.get(Gateway.scrt), dstEnum.get(Gateway.scrt));
		assertEquals("源和目标gw不相等", srcEnum.get(Gateway.active), dstEnum.get(Gateway.active));
	}

}
