package com.hundsun.hsccbp.nlp.transor;

import gnu.trove.iterator.TObjectIntIterator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.fnlp.ml.classifier.linear.Linear;
import org.fnlp.ml.types.alphabet.AlphabetFactory;
import org.fnlp.ml.types.alphabet.LabelAlphabet;
import org.fnlp.ml.types.alphabet.StringFeatureAlphabet;
import org.fnlp.nlp.cn.tag.CWSTagger;
import org.fnlp.nlp.pipe.seq.templet.TempletGroup;
import org.fnlp.util.exception.LoadModelException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 以文件为载体的转换器，对象从文件中去除，转换的对象也保存在文件中
 * @author pengqb
 *
 */
public class FileTransor extends AbstractTransor{
	protected String writeValue() throws IOException, LoadModelException{
		String modelFile = "models/pos.m";
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		CWSTagger seg = new CWSTagger(modelFile);
		mapper.writeValue(new File("models/cwstagger.json"), seg);
		Linear linear = seg.getClassifier();
		mapper.writeValue(new File("models/linear.json"), linear);
		TempletGroup templets = seg.getTemplets();
		mapper.writeValue(new File("models/templets.json"), templets);
		AlphabetFactory factory = linear.getAlphabetFactory();
		mapper.writeValue(new File("models/factory.json"), factory);
		StringFeatureAlphabet features = (StringFeatureAlphabet)factory.DefaultFeatureAlphabet();
		Map<Integer,String> dataMap = new TreeMap<Integer,String>();
		TObjectIntIterator<String> it = features.iterator();
		while(it.hasNext()){
			it.advance();
			String value = it.key();
			int key = it.value();
			dataMap.put(key, value);
		}
		mapper.writeValue(new File("models/dataMap.json"), dataMap);
		LabelAlphabet labels = factory.DefaultLabelAlphabet();
		mapper.writeValue(new File("models/labels.json"), labels);
		return "查看文件models/linear.json";
	}

	@Override
	protected String readValue() {
		// TODO 代码未完成
		return null;
	}
}
