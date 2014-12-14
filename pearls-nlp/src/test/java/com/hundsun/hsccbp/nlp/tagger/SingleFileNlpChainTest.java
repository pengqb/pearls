package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;
import com.hundsun.hsccbp.nlp.extracts.ExtractResult;

public class SingleFileNlpChainTest {
	transient final ExtractConfig extractConfig = new ExtractConfig();
	transient private NlpWrap nlper;
	@Before
	public void initialize(){
		final Path path = FileSystems.getDefault().getPath("E:\\nlp\\shiyan\\sina\\mirror\\finance.sina.com.cn\\china\\20141116\\015020829745.prl");
		nlper = new SingleFileNlpChain(path, extractConfig,
				extractConfig.getModelFilePath());
	}
	@Test
	public void testTag() {
		ExtractResult er = nlper.nlp();
		assertTrue(CExtract.SUCCESS_CODE.equals(er.getCode()));
	}
	
}
