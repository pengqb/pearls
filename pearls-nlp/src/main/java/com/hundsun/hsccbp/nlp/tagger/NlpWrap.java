package com.hundsun.hsccbp.nlp.tagger;

import java.io.IOException;
import java.nio.file.Path;

import org.fnlp.util.exception.LoadModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;
import com.hundsun.hsccbp.nlp.extracts.ExtractResult;

/**
 * 自然语言处理包装器，包装了fnlp处理的入口，包括分词、词性标注、实体名标注、依赖句法分析
 * @author pengqb
 *
 */
public abstract class NlpWrap {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SingleFileNlpChain.class);
	/**
	 * 被抽取的文件的路径,可能是一个文件所在的路径，也可能是一个目录所在的路径
	 */
	private Path filePath;
	
	private String modelFilePath;

	transient protected ExtractConfig extractConfig;

	private CNFactory cnFactory;

	public NlpWrap(final Path filePath, final ExtractConfig extractConfig,
			String modelFilePath){
		this.filePath = filePath;
		this.extractConfig = extractConfig;
		this.modelFilePath = modelFilePath;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(final Path filePath) {
		this.filePath = filePath;
	}

	public CNFactory getCnFactory() {
		return cnFactory;
	}

	public void setCnFactory(CNFactory cnFactory) {
		this.cnFactory = cnFactory;
	}

	/**
	 * 自然语言处理,包括分词、词性标注、实体名标注、依赖句法分析
	 * @return
	 */
	public ExtractResult nlp(){
		String code = CExtract.SUCCESS_CODE;
		String msg = "";
		try {
			this.cnFactory = CNFactory.getInstance(modelFilePath);
			this.posTag();
			this.jointParse();
			this.nerTag();
			this.coreferenceResolution();
		} catch (Exception e) {
			code = CExtract.FAIL_CODE;
			msg = "执行词性标注的过程中发生错误，请联系系统管理员";
			LOGGER.error(msg, e);
		}
		return new ExtractResult(code, msg);
	}
	/**
	 * 分词和词性标注
	 * @throws IOException
	 */
	abstract protected void posTag() throws IOException;
	/**
	 * 实体名标注
	 * @throws IOException
	 */
	abstract protected void nerTag() throws IOException;
	/**
	 * 依赖句法分析
	 * @throws IOException
	 */
	abstract protected void jointParse() throws Exception;
	/**
	 * 指代消解
	 * @throws IOException
	 */
	abstract protected void coreferenceResolution() throws IOException;
	
	/**
	 * 情感分析
	 * @throws IOException
	 */
	abstract protected void sentimentAnalysis() throws IOException;
}
