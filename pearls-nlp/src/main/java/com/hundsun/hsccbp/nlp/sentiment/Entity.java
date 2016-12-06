package com.hundsun.hsccbp.nlp.sentiment;

/**
 * 系统应用中的关心的实体
 * 如大盘/上证综指/深证成指/沪市大盘/深成指/两市股指/版块/个股/股评人/原油/黄金等
 * @author pengqb
 * 
 */
public class Entity {
	/**
	 * 实体名
	 */
	private String name;

	/**
	 * 实体类型
	 */
	private EntityType type;

	/**
	 * 同义实体对应的同义词
	 */
	private Entity next;
	
	public Entity(String name,EntityType type,Entity entity){
		this.name = name;
		this.type = type;
		this.next = entity;
	}

	/**
	 * 实体同义词消解，找到最终的根实体
	 * 
	 * @return
	 */
	public Entity finRoot() {
		Entity entity = this;
		do {
			if (entity.type.equals(EntityType.root)) {
				break;
			}
			entity = entity.next;
		} while (entity.next != null);
		return entity;
	}

	/**
	 * 得到从同义实体到根实体的路径
	 * @return
	 */
	public String getPath() {
		Entity entity = this;
		String path = "/" + entity.name;
		while (entity.next != null) {
			entity = entity.next;
			path += "/" + entity.name;
		}
		return path;
	}

}
