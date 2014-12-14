package com.hundsun.hsccbp.nlp.sentiment;

/**
 * 词和词性，判断一个词的意义需要根据词本身和词性两个属性来判断 例如：应监管层要求。股市应会上涨
 * 
 * @author pengqb
 * 
 */
public class WordPos {
	/**
	 * 单词，如冲高
	 */
	private String word;
	/**
	 * 词性，如动词
	 */
	private String pos;
	
	public WordPos() {
		
	}

	public WordPos(String word, String pos) {
		this.word = word;
		this.pos = pos;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return word + "[" + pos + "]";
	}

	@Override
	public int hashCode() {
		return (word + "[" + pos + "]").hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		WordPos other = (WordPos) obj;
		return (this.word + "[" + this.pos + "]").equals(other.getWord() + "["
				+ other.getPos() + "]");
	}

}
