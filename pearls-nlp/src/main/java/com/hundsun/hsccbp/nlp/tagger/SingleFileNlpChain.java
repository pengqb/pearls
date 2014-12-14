package com.hundsun.hsccbp.nlp.tagger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;

import org.fnlp.nlp.cn.Sentenizer;
import org.fnlp.nlp.parser.dep.DependencyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;

/**
 * 对单个文件执行Nlp过程
 * 
 * @author pengqb
 * 
 */
public class SingleFileNlpChain extends NlpWrap {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SingleFileNlpChain.class);

	private transient SentenceJointParser sentenceJointParser;

	public SingleFileNlpChain(final Path filePath,
			final ExtractConfig extractConfig, String modelFilePath) {
		super(filePath, extractConfig, modelFilePath);
		sentenceJointParser = new SentenceJointParser(
				CNFactory.getInstance(modelFilePath));
	}

	@Override
	protected void posTag() throws IOException {
		String file = this.getFilePath().toString();
		String s = getCnFactory().getPos().tagFile(file);
		String output = file.replaceFirst(CExtract.EXTRACTED_FILE_EXTEND,
				CExtract.POSED_FILE_EXTEND);
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
				output), CExtract.FILE_CHARSET_UTF8);
		w.write(s);
		w.close();
	}

	@Override
	protected void nerTag() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void jointParse() throws Exception {
		String file = this.getFilePath().toString();
		String content = readFile(file);
		String[] sents = Sentenizer.split(content);
		String output = file.replaceFirst(CExtract.EXTRACTED_FILE_EXTEND,
				CExtract.JOINTPARSED_FILE_EXTEND);
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
				output), CExtract.FILE_CHARSET_UTF8);
		int i = 0;
		for (String sent : sents) {
			i++;
			List<List<String>> dependencyList = sentenceJointParser
					.jointParse(sent);
			LOGGER.debug(dependencyList.toString());
			w.write(dependencyList.toString());
			if (i < sents.length) {
				w.write(CExtract.NEWLINE);
			}
		}
		w.close();
	}

	private String readFile(String file) {
		String str = "";
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), CExtract.FILE_CHARSET_UTF8);
			BufferedReader lbin = new BufferedReader(read);
			String line = lbin.readLine();
			while (line != null) {
				str += line;
				line = lbin.readLine();
			}
			lbin.close();
		} catch (IOException e) {
			LOGGER.error("读输入文件错误", e);
		}
		System.out.println(str);
		return str;
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
