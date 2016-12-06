package com.hundsun.hsccbp.nlp.tagger;

import static org.junit.Assert.assertTrue;

import org.fnlp.nlp.parser.dep.DependencyTree;
import org.junit.Before;
import org.junit.Test;

import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;

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
		DependencyTree dependencyTree = sentenceJointParser.jointParse("复旦大学创建于1905年,他位于上海市，这个大学培育了好多优秀的学生。");
		assertTrue(dependencyTree.size() >0);
	}

}
