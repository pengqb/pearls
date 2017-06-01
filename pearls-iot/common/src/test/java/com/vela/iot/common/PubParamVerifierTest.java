package com.vela.iot.common;

import java.util.EnumMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PubParamVerifierTest {

	static Map<Param, Object> params;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		params = new EnumMap<>(Param.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testVerify() {
		params.put(Param.a, "dev/active");
		params.put(Param.v, "2.1.4");
		params.put(Param.t, "1494398597000");
		params.put(Param.k, "PyUE4E+JEdOaDAMF6CwzAQ");
		params.put(Param.l, "zh_cn");
		params.put(Param.s, "PyUE4E+JEdOaDAMF6CwzAQ");
		PubParamVerifier.getInstance().verify(params);
	}
	
	@Test
	public void testVerifyNullException() {
		params.put(Param.a, null);
		thrown.expect(Exception400.class);
		PubParamVerifier.getInstance().verify(params);
	}
	
	@Test
	public void testVerifyLengthException() {
		params.put(Param.a, "dev/active");
		params.put(Param.v, "10.10.10.10");
		thrown.expect(Exception400.class);
		PubParamVerifier.getInstance().verify(params);
	}
}
