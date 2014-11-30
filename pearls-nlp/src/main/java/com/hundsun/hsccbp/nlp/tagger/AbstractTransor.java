package com.hundsun.hsccbp.nlp.tagger;

/**
 * 转换器抽象类，把对象从一个格式转换从另外一种格式
 * 如从对象格式转换成json格式。
 * @author pengqb
 *
 */
public abstract class AbstractTransor {

	public AbstractTransor() {
		super();
	}

	/**
	 * 对象转换成Json
	 * @return
	 */
	public boolean ObjectToJson() {
		boolean transSuccess = true;
		try {
			String jsonString = writeValue();
			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			transSuccess = false;
		}
		return transSuccess;
	}
	
	protected abstract String writeValue()throws Exception;

}