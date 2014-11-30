package com.hundsun.hsccbp.nlp.extracts;

import static org.junit.Assert.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class ComplexExtractorTest {
	transient private ComplexExtractor complexExtractor;
	transient final ExtractConfig extractConfig = new ExtractConfig();

	@Before
	public void initialize(){
		final Path filePath = FileSystems.getDefault().getPath("E:\\green\\heritrix-3.2.0\\bin\\jobs\\shiyan\\sina\\mirror\\finance.sina.com.cn\\china");
		complexExtractor = new ComplexExtractor(filePath,extractConfig);
	}

	@Test
	public void testExtract() {
		ExtractResult result = complexExtractor.extract();
		assertEquals(result.getDestPath(), CExtract.SUCCESS_CODE, result.getCode());
	}

}
