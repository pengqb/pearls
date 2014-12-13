package com.hundsun.hsccbp.nlp.sentiment;

import java.util.Date;

/**
 * 时间词
 * @author pengqb
 *
 */
public class WordTime {
	/**
	 * 表示时间的单词，
	 * 如明天/下周/下个月/下半年/明年/马上/即将
	 * 时间范围 短期/长期/后市/后续/
	 */
	private String word;
	private Date startTime;
	private Date endTime;
}
