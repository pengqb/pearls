package com.vela.iot.common.security;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vela.iot.common.PerfTest;
import com.vela.iot.common.security.SecurityCode.SecurityCodeLevel;

import static com.vela.iot.common.security.SecurityCode.*;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecurityCodePerfTest extends PerfTest {
	@Test
	public void aaaa() {
		SecurityCode.randomSixPwd();
		SecurityCode.getSecurityCode(8, SecurityCodeLevel.Hard, false);
		SecurityCode.isValidPwd(SecurityCode.getSecurityCode(8,
				SecurityCodeLevel.Hard, false));
		SecurityCode.getUUIDbyStr();
		SecurityCode.getUUIDbyLong();
		SecurityCode.string2MD5("111111");
		SecurityCode.string2SHA256("111111");
	}

	@Test(timeout = 100)
	public void testRandomSixPwd() {
		for (int i = 0; i < count; i++) {
			SecurityCode.randomSixPwd();
		}
	}

	@Test(timeout = 100)
	public void testGetSecurityCode() {
		for (int i = 0; i < count; i++) {
			SecurityCode.getSecurityCode(8, SecurityCodeLevel.Hard, false);
		}
	}

	@Test(timeout = 100)
	public void testIsValidPwd() {
		for (int i = 0; i < count; i++) {
			SecurityCode.isValidPwd(SecurityCode.getSecurityCode(8,
					SecurityCodeLevel.Hard, false));
		}
	}
	
	@Test(timeout = 150)
	public void testZetUUIDbyLong(){
		for (int i = 0; i < count; i++) {
			getUUIDbyLong();
		}
	}

	@Test
	public void testGetUUIDbyStr(){
		for (int i = 0; i < count; i++) {
			getUUIDbyStr();
		}
	}
	
	@Test
	public void testString2MD5(){
		String plainText = "111111";
		for (int i = 0; i < count; i++) {
			SecurityCode.string2MD5(plainText);
		}
	}
	
	@Test
	public void testString2SHA256(){
		String plainText = "111111";
		for (int i = 0; i < count; i++) {
			SecurityCode.string2SHA256(plainText);
		}
	}
	
}
