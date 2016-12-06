package com.hundsun.hsccbp.nlp.sentiment;

import java.util.List;

/**
 * 表示一个实体，在某个时间点或某个时间范围内的情感评分
 * 当一个实体无法判断时，扔掉该实体。这和entity.score = 0 是有区别的
 * @author pengqb
 *
 */
public class EntitySentimentModel {

	private WordTime wordTime;
	
	private Entity entity;
	
	/**
	 * 表示实体的情感得分
	 * +表示积极情感
	 * -表示消极情感
	 * 0表示中性，
	 */
	private Integer score;
	
	private List<String> arguments;
	
	/**
	 * 得到实体对应的表示情感的颜色，
	 * 例如可以红色表示积极的，绿色表示消极的。
	 * 用颜色的深浅表示程度
	 * @return
	 */
	public Integer getColor(){
		//TODO 未完成代码
		return score;
	}
}
