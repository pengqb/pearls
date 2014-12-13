package com.hundsun.hsccbp.nlp.tagger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.hsccbp.nlp.extracts.CExtract;
import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;

/**
 * 对单个文件执行Nlp过程
 * @author pengqb
 *
 */
public class SingleFileNlpChain extends NlpWrap {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SingleFileNlpChain.class);

	public SingleFileNlpChain(final Path filePath,
			final ExtractConfig extractConfig, String modelFilePath) {
		super(filePath, extractConfig, modelFilePath);
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
		// TODO Auto-generated method stub
		
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
