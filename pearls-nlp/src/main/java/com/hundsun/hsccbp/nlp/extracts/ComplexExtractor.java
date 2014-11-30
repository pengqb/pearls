package com.hundsun.hsccbp.nlp.extracts;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplexExtractor extends AbstractExtractor {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SingleExtractor.class);

	private transient List<String> fileList = new ArrayList<String>();

	public ComplexExtractor(final Path filePath,
			final ExtractConfig extractConfig) {
		super(filePath, extractConfig);
		fileList = ExtractUtil.listDepthFiles(filePath.toString());
	}

	@Override
	public ExtractResult extract() {
		String code = CExtract.SUCCESS_CODE;
		String msg = "";
		int num = 0;// 统计正确抓取的文件个数
		for (String file : fileList) {
			final java.nio.file.Path filePath = FileSystems.getDefault()
					.getPath(file);
			final SingleExtractor extractor = new SingleExtractor(filePath,
					extractConfig);
			ExtractResult extractResult = extractor.extract();
			if (CExtract.SUCCESS_CODE.equals(extractResult.getCode())) {
				num++;
			}
		}
		// 全部正确抓取
		if (num == fileList.size()) {
			code = CExtract.SUCCESS_CODE;
			msg = "全部正确抓取，抓取文件个数：" + fileList.size();
		} else {
			code = CExtract.FAIL_CODE;
			msg = "正确抓取文件个数：" + num + ",错误抓取文件个数：" + (fileList.size() - num);
		}
		return new ExtractResult(code,msg); 
	}

}
