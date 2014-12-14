package com.hundsun.hsccbp.nlp.sentiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 单词模型,表示情感，实体相关词的集合
 * 
 * @author pengqb
 * 
 */
public final class WordModel {
	static private WordModel wordModel;
	/**
	 * 积极性词,如翻红/向上/看涨/强势/做多/上涨/冲高/反弹/走牛/牛市/看多/上攻/突破/攀升/乐观/阳线/利好/买入/希望
	 */
	private Map<WordPos, Integer> postives = new HashMap<WordPos, Integer>();
	/**
	 * 消极性词，如破位/下跌/翻绿/向下/看跌/阴跌/弱势/做空/回落/飘绿/调整/熊市/看空/低开/走低/利空/悲观/萎缩/割肉/卖出/害怕/担心
	 */
	private Map<WordPos, Integer> negatives = new HashMap<WordPos, Integer>();
	/**
	 * 否定词，如不/未/尚未/没有
	 */
	private Map<WordPos, Integer> nots = new HashMap<WordPos, Integer>();
	/**
	 * 可能词 副词，如可能/很可能/也许/推测/应该/应/近乎/想必/有望/概率/机会/大致/或许/必定/势必/一定
	 */
	private Map<WordPos, Integer> mays = new HashMap<WordPos, Integer>();
	/**
	 * 系数词，如明显/小幅/大幅/相当/最/非常/强烈/更
	 */
	private Map<WordPos, Integer> coefficients = new HashMap<WordPos, Integer>();

	/**
	 * 实体词，表示系统要关注的实体 如大盘/上证综指/深证成指/沪市大盘/深成指/两市股指/版块/个股/股评人/原油/黄金等
	 */
	private Set<Entity> entities = new HashSet<Entity>();

	private WordModel() {
		WordPos 大涨 = new WordPos("大涨","动词");
		postives.put(大涨, +90);
		
		WordPos 不 = new WordPos("不","副词");
		nots.put(不, -100);
		
		WordPos 可能 = new WordPos("可能","情态词");
		mays.put(可能, +50);
		
		WordPos 会 = new WordPos("会","情态词");
		mays.put(会, +100);
		
		Entity entity = new Entity("大盘",EntityType.root,null);
		entities.add(entity);
		
		
	}

	public static WordModel getInstance() {
		if (wordModel == null) {
			wordModel = new WordModel();
		}
		return wordModel;
	}
	
	/**
	 * 判断一个词是否是情感词
	 * @param wordPos
	 * @return
	 */
	public boolean isSentiment(WordPos wordPos){
		boolean isSentiment = false;
		if(postives.containsKey(wordPos)){
			isSentiment = true;
		}
		if(negatives.containsKey(wordPos)){
			isSentiment = true;
		}
		return isSentiment;
	}
	
	/**
	 * 判断一个词是否是否定词
	 * @param wordPos
	 * @return
	 */
	public boolean isNot(WordPos wordPos){
		return isQualifier(wordPos,nots);
	}
	
	/**
	 * 判断一个词是否是可能词
	 * @param wordPos
	 * @return
	 */
	public boolean isMay(WordPos wordPos){
		return isQualifier(wordPos,mays);
	}
	
	/**
	 * 判断一个词是否是系数词
	 * @param wordPos
	 * @return
	 */
	public boolean isCoefficient(WordPos wordPos){
		return isQualifier(wordPos,coefficients);
	}
	
	/**
	 * 判断一个词是否是修饰词
	 * @param wordPos
	 * @param map
	 * @return
	 */
	private boolean isQualifier(WordPos wordPos,Map<WordPos, Integer> map){
		boolean isQualifier = false;
		if(map.containsKey(wordPos)){
			isQualifier = true;
		}
		return isQualifier;
	}
	
}
