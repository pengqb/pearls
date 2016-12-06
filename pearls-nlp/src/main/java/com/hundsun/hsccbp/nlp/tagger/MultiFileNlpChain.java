package com.hundsun.hsccbp.nlp.tagger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fnlp.nlp.parser.dep.train.JointParerTester;
import org.fnlp.util.MyCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;
import com.hundsun.hsccbp.nlp.extracts.ExtractUtil;

/**
 * 对多个文件执行Nlp过程
 * @author pengqb
 *
 */
public class MultiFileNlpChain extends NlpWrap {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MultiFileNlpChain.class);

	private transient List<String> fileList = new ArrayList<String>();

	public MultiFileNlpChain(final Path filePath,
			final ExtractConfig extractConfig, String modelFilePath){
		super(filePath, extractConfig, modelFilePath);
		fileList = ExtractUtil.listDepthFiles(filePath.toString());
	}

	@Override
	protected void posTag() throws IOException {
		for (String file : fileList) {
			String s = getCnFactory().getPos().tagFile(file);
			String output = file.replaceFirst(CExtract.EXTRACTED_FILE_EXTEND,
					CExtract.POSED_FILE_EXTEND);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
					output), CExtract.FILE_CHARSET_UTF8);
			w.write(s);
			w.close();
		}
	}

	@Override
	protected void nerTag() throws IOException {
		for (String file : fileList) {
			Map<String, String> map = getCnFactory().getNer().tagFile(file);
			String output = file.replaceFirst(CExtract.EXTRACTED_FILE_EXTEND,
					CExtract.NERRED_FILE_EXTEND);
			MyCollection.write(map.entrySet(), output);
		}
	}

	@Override
	protected void jointParse() throws Exception {
		String modelFile = extractConfig.getModelFilePath() + "/dep.m";
		// TODO
		// 由于fnlp.JointParser本身没有提供解析文件的功能，反而是在JointParerTester里提供了，所以这里解析文件先借用JointParerTester
		JointParerTester tester = new JointParerTester(modelFile);
		for (String file : fileList) {
			String output = file.replaceFirst(CExtract.EXTRACTED_FILE_EXTEND,
					CExtract.JOINTPARSED_FILE_EXTEND);
			tester.test(file, output, CExtract.FILE_CHARSET_UTF8);
		}
	}

	@Override
	protected void coreferenceResolution() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void sentimentAnalysis() throws IOException {
		// TODO Auto-generated method stub

	}

}
