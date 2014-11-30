package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Test;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;
import com.hundsun.hsccbp.nlp.extracts.ExtractResult;

public class ComplexNlpChainTest {
	transient final ExtractConfig extractConfig = new ExtractConfig();

	@Test
	public void testTag() {
		String filePath = "E:\\nlp\\shiyan\\sina\\mirror\\finance.sina.com.cn\\china\\20141116";
		final Path path = FileSystems.getDefault().getPath(filePath);
		NlpWrap posTagger = new ComplexNlpChain(path, extractConfig,
				extractConfig.getModelFilePath());
		ExtractResult er = posTagger.nlp();
		assertTrue(CExtract.SUCCESS_CODE.equals(er.getCode()));
	}

}
