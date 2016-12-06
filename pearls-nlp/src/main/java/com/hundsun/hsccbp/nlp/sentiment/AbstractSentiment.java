package com.hundsun.hsccbp.nlp.sentiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractSentiment {

	protected WordModel wordModel = WordModel.getInstance();
	protected List<EntitySentimentModel> entityModel = new ArrayList<EntitySentimentModel>();
	/**
	 * 主动推荐得分位于startScore和endScore之间的实体
	 * @param startScore
	 * @param endScore
	 * @return
	 */
	public List<EntitySentimentModel> recoment(Integer startScore,Integer endScore){
		//TODO
		return entityModel;
	}
	
	/**
	 * 主动推荐num位情感偏于积极的实体
	 * @param num
	 * @return
	 */
	public List<EntitySentimentModel> recomentPostive(Integer num){
		//TODO
		return entityModel;
	} 
	
	/**
	 * 主动推荐num位情感偏于消极的实体
	 * @param num
	 * @return
	 */
	public List<EntitySentimentModel> recomentNegative(Integer num){
		//TODO
		return entityModel;
	} 
	
	/**
	 * 打印实体的颜色
	 */
	public void printEntitysColor(){
		//TODO
	}
	
	/**
	 * 找到支持该实体情感的所有论据
	 * @param entity
	 * @return 论据对应每篇文章，每句话的唯一标识
	 */
	public Map<String,List<String>> findArgument(Entity entity){
		//TODO
		return null;
	}
}
