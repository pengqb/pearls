package com.hundsun.hsccbp.nlp.transor;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileTransorTest {

	@Test
	public void testObjectToJson() {
		AbstractTransor transor = new FileTransor();
		boolean isSuccess = transor.objectToJson();
		assertTrue("对象转换成json，是否成功？isSuccess = ",isSuccess);
	}

}
