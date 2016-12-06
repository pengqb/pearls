package com.hundsun.hsccbp.nlp.transor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTransorTest {

	@Test
	public void testObjectToJson() {
		AbstractTransor transor = new MemoryTransor();
		boolean isSuccess = transor.objectToJson();
		assertTrue("对象转换成json，是否成功？isSuccess = ",isSuccess);
	}

}
