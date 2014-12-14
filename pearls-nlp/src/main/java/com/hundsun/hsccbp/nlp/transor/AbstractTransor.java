package com.hundsun.hsccbp.nlp.transor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转换器抽象类，把对象从一个格式转换成另外一种格式
 * 如从对象格式转换成json格式。
 * @author pengqb
 *
 */
public abstract class AbstractTransor {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(AbstractTransor.class);

	public AbstractTransor() {
		super();
	}

	/**
	 * 对象转换成Json
	 * @return
	 */
	public boolean objectToJson() {
		boolean transSuccess = true;
		try {
			writeValue();
		} catch (Exception e) {
			LOGGER.error("对象转换成json格式发生错误", e);
			transSuccess = false;
		}
		return transSuccess;
	}
	
	public boolean jsonToObject(){
		boolean transSuccess = true;
		try {
			readValue();
		} catch (Exception e) {
			LOGGER.error("json格式转换成对象发生错误", e);
			transSuccess = false;
		}
		return transSuccess;
	}
	
	protected abstract String writeValue()throws Exception;
	
	protected abstract String readValue();

}