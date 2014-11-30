package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaggerJsonTransorTest {

	@Test
	public void testObjectToJson() {
		AbstractTransor transor = new FileTransor();
		boolean isSuccess = transor.ObjectToJson();
		assertTrue("对象转换成json，是否成功？isSuccess = ",isSuccess);
	}

}
