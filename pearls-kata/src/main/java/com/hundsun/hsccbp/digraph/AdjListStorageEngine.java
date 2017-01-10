package com.hundsun.hsccbp.digraph;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有向图邻接表存储引擎
 * @author pengqb
 *
 */
public class AdjListStorageEngine {
	private Map<String, Map<String,Integer>> graphStore = new ConcurrentHashMap<>();
		
	public Map<String, Map<String, Integer>> getGraphStore() {
		return graphStore;
	}

	public void setGraphStore(Map<String, Map<String, Integer>> graphStore) {
		this.graphStore = graphStore;
	}

	/**
	 * 初始化有向图数据
	 * @param graphStr
	 * @throws Exception
	 */
	public void initStore(String graphStr) throws Exception{
		if(!StringUtils.initGraphStrMatchs(graphStr)){
			throw new Exception(StringUtils.initGraphStrError);
		}
		String[] element = graphStr.split(",");
		List<String> lists = Arrays.asList(element);
		for (String list : lists) {
			char[] chars = list.toCharArray();
			Map<String,Integer> linkedMap = graphStore.getOrDefault(String.valueOf(chars[0]),
					new ConcurrentHashMap<String,Integer>());
			linkedMap.put(String.valueOf(chars[1]), Integer.parseInt(String.valueOf(chars[2])));
			graphStore.putIfAbsent(String.valueOf(chars[0]), linkedMap);
		}
	}
}
