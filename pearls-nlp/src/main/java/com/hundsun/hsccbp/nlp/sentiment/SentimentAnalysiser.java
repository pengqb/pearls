package com.hundsun.hsccbp.nlp.sentiment;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 情感分析器，对依存句法树进行情感分析
 * @author pengqb
 *
 */
public class SentimentAnalysiser {
	private transient List<List<List<String>>> fileDependencyList;
	private transient ObjectMapper mapper = new ObjectMapper(); // create once, reuse
	
//	public List<List<List<String>>> readDependencyListFrimFile(){
//		fileDependencyList = mapper.readValue(new File(output), fileDependencyList.getClass());
//	}
}
