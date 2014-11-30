package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectJsonTransorTest {

	@Test
	public void testObjectToJson() {
		AbstractTransor transor = new MemoryTransor();
		boolean isSuccess = transor.ObjectToJson();
		assertTrue("对象转换成json，是否成功？isSuccess = ",isSuccess);
	}

}
