package com.hundsun.hsccbp.nlp.extracts;

import io.dropwizard.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author pengqb
 */
public class ExtractConfig extends Configuration {
	private String template;
	private String rawHome ="E:\\green\\heritrix-3.2.0\\bin\\jobs";
	private String extractedHome = "E:\\nlp";
	private String rawPath = "E:\\green\\heritrix-3.2.0\\bin\\jobs\\shiyan\\sina\\mirror\\finance.sina.com.cn\\china\\20141116\\015020829745.shtml";

	private String modelFilePath = "D:\\pearlsws\\pearls\\pearls-nlp\\models";
	
	/**
	 * 用于保存不同域对应文件的抽取规则<br>
	 * 通常，相同域的下的网页放在相同的本地文件夹下，例如新浪财经的网页都会放在文件夹finance.sina.com.cn下面。<br>
	 * 另一方面不同域下面的网页抽取的规则不同。
	 */
	private Map<String, Pattern> ruleMap;

	public ExtractConfig() {
		super();
		ruleMap = new HashMap<String, Pattern>();
		// 因为被抓取的网页包含多行，表达式 . 希望可以匹配任何字符，包括行结束符。
		ruleMap.put("sina.com.cn", Pattern.compile(
				"publish\\_helper.*publish\\_helper\\_end", Pattern.DOTALL));
		ruleMap.put("ifeng.com", Pattern.compile("edf", Pattern.DOTALL));
		ruleMap.put("baidu.com", Pattern.compile("ghk", Pattern.DOTALL));
		ruleMap.put("hexun.com", Pattern.compile("lkp", Pattern.DOTALL));
	}

	//@JsonProperty
	//@JsonAnyGetter
	public Map<String, Pattern> getRuleMap() {
		return ruleMap;
	}

	//@JsonProperty
	public void setRuleMap(final Map<String, Pattern> ruleMap) {
		this.ruleMap = ruleMap;
	}

	@JsonProperty
	public String getTemplate() {
		return template;
	}

	@JsonProperty
	public void setTemplate(String template) {
		this.template = template;
	}
	
	//@JsonProperty
	public String getRawHome() {
		return rawHome;
	}

	//@JsonProperty
	public void setRawHome(String rawHome) {
		this.rawHome = rawHome;
	}

	//@JsonProperty
	public String getExtractedHome() {
		return extractedHome;
	}

	//@JsonProperty
	public void setExtractedHome(String extractedHome) {
		this.extractedHome = extractedHome;
	}

	@JsonProperty
	public String getRawPath() {
		return rawPath;
	}

	@JsonProperty
	public void setRawPath(String rawPath) {
		this.rawPath = rawPath;
	}
	@JsonProperty
	public String getModelFilePath() {
		return modelFilePath;
	}
	@JsonProperty
	public void setModelFilePath(String modelFilePath) {
		this.modelFilePath = modelFilePath;
	}
}
