package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;
import com.hundsun.hsccbp.nlp.extracts.ExtractResult;

public class SentenceNlpChainTest {
	transient final ExtractConfig extractConfig = new ExtractConfig();
	SentenceJointParser sentenceJointParser;
	@Before
	public void initialize(){
		CNFactory cnFactory = CNFactory.getInstance(extractConfig.getModelFilePath());
		sentenceJointParser = new SentenceJointParser(cnFactory);
	}

	
	@Test
	public void testJointParse() {
		List<List<String>> dependencyList = sentenceJointParser.jointParse("复旦大学创建于1905年,他位于上海市，这个大学培育了好多优秀的学生。");
		assertTrue(!dependencyList.isEmpty());
	}

}
