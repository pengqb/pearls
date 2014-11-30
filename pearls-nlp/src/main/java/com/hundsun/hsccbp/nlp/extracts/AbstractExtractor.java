package com.hundsun.hsccbp.nlp.extracts;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 文件抽取器，抽象类
 * 
 * @author pengqb
 * 
 */
public abstract class AbstractExtractor {
	/**
	 * 被抽取的文件的路径,可能是一个文件所在的路径，也可能是一个目录所在的路径
	 */
	protected Path filePath;

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(final Path filePath) {
		this.filePath = filePath;
	}

	/**
	 * 文件抽取器用到的配置信息
	 */
	transient protected ExtractConfig extractConfig;

	public AbstractExtractor(final Path filePath,final ExtractConfig extractConfig) {
		this.filePath = filePath;
		this.extractConfig = extractConfig;
	};

	/**
	 * 抽取文件的部分内容生成新的文件
	 */
	public abstract ExtractResult extract();
}
