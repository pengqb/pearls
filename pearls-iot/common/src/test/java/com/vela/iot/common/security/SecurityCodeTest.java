package com.vela.iot.common.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vela.iot.common.security.SecurityCode.SecurityCodeLevel;

public class SecurityCodeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsValidPwd() {
		assertTrue(SecurityCode.isValidPwd("123456"));
		assertFalse(SecurityCode.isValidPwd("er3rty"));
	}

	@Test
	public void testGetSecurityCode() {
		String str6 = SecurityCode.getSecurityCode(6, SecurityCodeLevel.Simple,
				false);
		assertTrue(SecurityCode.isValidPwd(str6));
		String str8 = SecurityCode.getSecurityCode(16, SecurityCodeLevel.Hard,
				false);
		assertTrue(SecurityCode.isValidPwd(str8));
	}

	@Test
	public void testRandomSixPwd() {
		String str6 = SecurityCode.randomSixPwd();
		System.out.println(str6);
	}

	@Test
	public void testGetUUIDbyLong() {
		String uuidbase64 = SecurityCode.getUUIDbyLong();
		assertEquals("SecurityCode.getUUIDbyLong uuid长度不正确",
				uuidbase64.length(), 22);
	}

	@Test
	public void testGetUUIDbyStr() {
		String uuidbase64 = SecurityCode.getUUIDbyStr();
		assertEquals("SecurityCode.getUUIDbyStr uuid长度不正确",
				uuidbase64.length(), 22);
	}

	@Test
	public void testString2MD5() {
		String plainText = "q1w2e3";
		String ciphertext = SecurityCode.string2MD5(plainText);
		assertEquals("MD5签名错误", "7cbb3252ba6b7e9c422fac5334d22054", ciphertext);
	}

	@Test
	public void testString2SHA256() {
		String plainText = "q1w2e3";
		String ciphertext = SecurityCode.string2SHA256(plainText);
		assertEquals(
				"SHA256签名错误",
				"ae5a853873043c7b011c6300c464d8d4014bf833697a3c01817d83aa91a53166",
				ciphertext);
	}
}
