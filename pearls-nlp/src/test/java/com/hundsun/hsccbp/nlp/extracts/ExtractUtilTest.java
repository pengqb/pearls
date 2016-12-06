package com.hundsun.hsccbp.nlp.extracts;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

public class ExtractUtilTest {

	@Test
	public void testListDepthFiles() {
		final Path filePath = FileSystems.getDefault().getPath("E:", "green",
				"heritrix-3.2.0", "bin", "jobs", "shiyan", "sina", "mirror");
		List<String> fileList = ExtractUtil.listDepthFiles(filePath.toString());
		// System.out.println("递归文件数：" + fileList.size());
		assertTrue("递归文件数大于0", !fileList.isEmpty());
	}

	@Test
	public void testMkdirs() {
		final Path filePath = FileSystems.getDefault().getPath("E:", "nlp",
				"zhang.txt");
		final boolean mkSuccess = ExtractUtil.mkdirs(filePath);
		assertTrue("创建目录", mkSuccess);
	}

	@Test
	public void testChangeCharset() throws UnsupportedEncodingException {
		String dest = ExtractUtil.changeCharset("张三",
				CExtract.FILE_CHARSET_UTF8, CExtract.FILE_CHARSET_UTF8);
		
	}
}
