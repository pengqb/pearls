package com.hundsun.hsccbp.digraph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilsTest {


	@Test
	public void testRouteStrMatchsMatchs() {
		String str = "A-B";
		assertEquals(true, StringUtils.routeStrMatchs(str));
		str = "A-B-C";
		assertEquals(true, StringUtils.routeStrMatchs(str));
		str = "A";
		assertEquals(false, StringUtils.routeStrMatchs(str));
		str = "A-";
		assertEquals(false, StringUtils.routeStrMatchs(str));
		str = "c-";
		assertEquals(false, StringUtils.routeStrMatchs(str));
	}
	
	@Test
	public void testInitGraphStrMatchs() {
		String initGraphStr = "AB5";
		assertEquals(true, StringUtils.initGraphStrMatchs(initGraphStr));
		initGraphStr = "AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7";
		assertEquals(true, StringUtils.initGraphStrMatchs(initGraphStr));
		initGraphStr = "AB5,";
		assertEquals(false, StringUtils.initGraphStrMatchs(initGraphStr));
		initGraphStr = "AF5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7";
		assertEquals(false, StringUtils.initGraphStrMatchs(initGraphStr));
		initGraphStr = "AF5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE-7";
		assertEquals(false, StringUtils.initGraphStrMatchs(initGraphStr));
	}
	
	@Test
	public void testCircleCheck() {
		assertEquals(true, StringUtils.circleCheck("BCDCD"));
	}

}
