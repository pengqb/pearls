package com.hundsun.hsccbp.nlp.tagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.fnlp.nlp.cn.anaphora.EntityGroup;
import org.fnlp.nlp.cn.ner.TimeUnit;
import org.fnlp.nlp.cn.ner.stringPreHandlingModule;
import org.fnlp.nlp.parser.dep.DependencyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对一句话执行JointParser
 * 
 * @author pengqb
 * 
 */
public class SentenceJointParser {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SentenceJointParser.class);
	private CNFactory cnFactory;

	public SentenceJointParser(CNFactory cnFactory) {
		this.cnFactory = cnFactory;
		// sentenceList.add("08年北京申办奥运会，8月8号开幕式，九月十八号闭幕式。");
		// sentenceList.add("今天我本想去世博会，但是人太多了，直到晚上9点人还是那么多。");
		// sentenceList.add("考虑到明天和后天人还是那么多，决定下周日再去。");
		// sentenceList.add("明年我的孩子就要出生了。");
		// sentenceList.add("中国进出口银行与中国银行加强合作。");
		// sentence = "复旦大学创建于1905年,他位于上海市，这个大学培育了好多优秀的学生。";

	}

	/**
	 * 对一个句子执行依赖句法分析，该依赖句法分析还包括了时间短语解析和指代消解
	 * 
	 * @param sentence
	 * @return
	 */
	protected DependencyTree jointParse(String sentence) {
		String[][] s = cnFactory.getPos().tag2Array(sentence);
		DependencyTree tree = cnFactory.getParser().parse2T(s[0], s[1]);
		// LOGGER.debug(tree.toString());
		dependencyTreeAttachTime(tree);
		List<List<String>> dependencyList = new ArrayList<List<String>>();
		dependencyList = tree.toList();
		coreferenceResolution(dependencyList, sentence);
		// LOGGER.debug(dependencyList.toString());
		return tree;
	}

	/**
	 * 对依赖树中的时间短语附加具体的时间
	 * 
	 * @param tree
	 * @throws IOException
	 */
	private void dependencyTreeAttachTime(DependencyTree tree) {
//		List<DependencyTree> timePhraseList = tree.getTimePhrase();
//		for (DependencyTree dt : timePhraseList) {
//			String timePhrash = "";
//			for (String str : dt.getWords(tree.size())) {
//				if (null != str) {
//					timePhrash += str;
//				}
//			}
//			String time = timeRecognite(timePhrash);
//			dt.time = time;
//		}
	}

	/**
	 * 把时间表达式替换成具体的时间
	 * 
	 * @param sentence
	 * @return
	 * @throws IOException
	 */
	private String timeRecognite(String sentence) {
		String transedStr = stringPreHandlingModule.numberTranslator(sentence);// 大写数字转化
		cnFactory.getTimeNormalizer().parse(transedStr);
		TimeUnit[] unit = cnFactory.getTimeNormalizer().getTimeUnit();
		for (int i = 0; i < unit.length; i++) {
			transedStr = transedStr.replaceFirst(unit[i].Time_Expression,
					unit[i].Time_Norm);
		}
		return transedStr;
	}

	/**
	 * 把指代消解的先行词放到依存句法树上
	 * 
	 * @param sentence
	 */
	private void coreferenceResolution(List<List<String>> dependencyList,
			String sentence) {
//		LinkedList<EntityGroup> ll = cnFactory.getRuleAnaphora().resolve(
//				sentence);
//		for (EntityGroup eg : ll) {
//			dependencyList.get(eg.anaphor.getStart()).set(6,
//					String.valueOf(eg.antecedent.getStart()));
//		}
	}
}
