package com.hundsun.hsccbp.nlp.tagger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fnlp.nlp.cn.anaphora.EntityGroup;
import org.fnlp.nlp.cn.ner.TimeUnit;
import org.fnlp.nlp.cn.ner.stringPreHandlingModule;
import org.fnlp.nlp.parser.dep.DependencyTree;

import com.hundsun.hsccbp.nlp.extracts.ExtractConfig;

/**
 * 对一句话执行Nlp过程
 * 
 * @author pengqb
 * 
 */
public class SentenceNlpChain extends NlpWrap {

	private transient List<String> sentenceList = new ArrayList<String>();
	
	// TODO Sentenizer 简单中文文本的断句
	private transient List<List<String>> dependencyList = new ArrayList<List<String>>();

	public SentenceNlpChain(final Path filePath,
			final ExtractConfig extractConfig, String modelFilePath) {
		super(filePath, extractConfig, modelFilePath);
		// sentenceList.add("08年北京申办奥运会，8月8号开幕式，九月十八号闭幕式。");
		// sentenceList.add("今天我本想去世博会，但是人太多了，直到晚上9点人还是那么多。");
		// sentenceList.add("考虑到明天和后天人还是那么多，决定下周日再去。");
		// sentenceList.add("明年我的孩子就要出生了。");

		// sentenceList.add("中国进出口银行与中国银行加强合作。");

		sentenceList.add("复旦大学创建于1905年,他位于上海市，这个大学培育了好多优秀的学生。");

	}

	@Override
	protected void posTag() throws IOException {
		for (String sentence : sentenceList) {
			String s = getCnFactory().getPos().tag(sentence);
			System.out.println(s);
		}
	}

	@Override
	protected void nerTag() throws IOException {
		// 实体名识别实际上在依赖句法分析中已经做了。
		// for (String sentence : sentenceList) {
		// Map<String, String> map = getCnFactory().getNer().tag(sentence);
		// System.out.println(map);
		// }
	}

	@Override
	protected void jointParse() throws Exception {
		for (String sentence : sentenceList) {
			String[][] s = getCnFactory().getPos().tag2Array(sentence);
			DependencyTree tree = getCnFactory().getParser()
					.parse2T(s[0], s[1]);
			System.out.println(tree.toString());
			List<DependencyTree> timePhraseList = tree.getTimePhrase();
			for (DependencyTree dt : timePhraseList) {
				String timePhrash = "";
				for (String str : dt.getWords(tree.size())) {
					if (null != str) {
						timePhrash += str;
					}
				}
				String time = timeRecognite(timePhrash);
				dt.time = time;
			}
			dependencyList.clear();
			dependencyList = tree.toList();
			coreferenceResolution(sentence);
			System.out.println(dependencyList);
		}
	}

	/**
	 * 把指代消解的先行词放到依存句法树上
	 * @param sentence
	 */
	private void coreferenceResolution(String sentence){
		LinkedList<EntityGroup> ll = getCnFactory().getRuleAnaphora()
				.resolve(sentence);
		for (EntityGroup eg : ll) {
			dependencyList.get(eg.anaphor.getStart()).set(6,
					String.valueOf(eg.antecedent.getStart()));
		}
	}
	
	@Override
	protected void coreferenceResolution() throws IOException {
		for (String sentence : sentenceList) {
			LinkedList<EntityGroup> ll = getCnFactory().getRuleAnaphora()
					.resolve(sentence);
			for (EntityGroup eg : ll) {
				System.out.println(eg);
			}
		}
	}

	/**
	 * 把时间表达式替换成具体的时间
	 * 
	 * @param sentence
	 * @return
	 * @throws IOException
	 */
	private String timeRecognite(String sentence) throws IOException {
		String transedStr = stringPreHandlingModule.numberTranslator(sentence);// 大写数字转化
		getCnFactory().getTimeNormalizer().parse(transedStr);
		TimeUnit[] unit = getCnFactory().getTimeNormalizer().getTimeUnit();
		for (int i = 0; i < unit.length; i++) {
			transedStr = transedStr.replaceFirst(unit[i].Time_Expression,
					unit[i].Time_Norm);
		}
		return transedStr;
	}

	@Override
	protected void sentimentAnalysis() throws IOException {
		// TODO Auto-generated method stub

	}

}
