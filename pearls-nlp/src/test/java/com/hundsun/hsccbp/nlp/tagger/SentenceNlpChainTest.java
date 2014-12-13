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

public class SentenceNlpChainTest {
	transient final ExtractConfig extractConfig = new ExtractConfig();
	NlpWrap sentenceNlpChain;
	@Before
	public void initialize(){
		String filePath = "E:\\nlp\\shiyan\\sina\\mirror\\finance.sina.com.cn\\china\\20141116\\015020829745.prl";
		final Path path = FileSystems.getDefault().getPath(filePath);
		sentenceNlpChain = new SentenceNlpChain(path, extractConfig,
				extractConfig.getModelFilePath());
	}

	@Test
	public void testPosTag() {
		fail("Not yet implemented");
	}

	@Test
	public void testNerTag() {
		fail("Not yet implemented");
	}

	@Test
	public void testJointParse() {
		fail("Not yet implemented");
	}

	@Test
	public void testCoreferenceResolution() {
		fail("Not yet implemented");
	}

	@Test
	public void testTimeRecognite() {
		fail("Not yet implemented");
	}

	@Test
	public void testSentimentAnalysis() {
		fail("Not yet implemented");
	}

	@Test
	public void testNlp() {
		ExtractResult er = sentenceNlpChain.nlp();
		assertTrue(CExtract.SUCCESS_CODE.equals(er.getCode()));
	}

}
