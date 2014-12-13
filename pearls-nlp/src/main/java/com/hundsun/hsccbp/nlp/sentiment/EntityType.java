package com.hundsun.hsccbp.nlp.sentiment;

public enum EntityType {
	/**
	 * 表示同一个实体的不同表述，如中国南车，sh601766
	 * 根实体 root，同义实体 synonym
	 * 什么词是根实体，什么词是同义实体，由模型约定。例如我们约定中国南车是根实体，sh601766是同义实体
	 */
	root, synonym
}
