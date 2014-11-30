package com.hundsun.hsccbp.nlp.tagger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 以内存为载体的转换器，对象从内存中取，转换的对象也保存在内存中
 * @author pengqb
 *
 */
public class MemoryTransor extends AbstractTransor {

	protected String writeValue() throws Exception{
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		// mapper.writeValue(new File("result.json"), myResultObject);
		// or:
		// byte[] jsonBytes = mapper.writeValueAsBytes(myResultObject);
		// or:
		
		List<String> sonList = new ArrayList<String>();
		sonList.add("张三1");
		sonList.add("张三2");
		Map<String,String> parentMap = new HashMap<String,String>();
		parentMap.put("mother", "赵一");
		parentMap.put("father", "钱二");
		MyValue myValue = new MyValue("张三",14,sonList,parentMap);
		String jsonString = mapper.writeValueAsString(myValue);
		return jsonString;
	}
}

class MyValue {
	private String name;
	private int age;
	private List<String> sonList;
	private Map<String, String> parentsMap;

	// NOTE: if using getters/setters, can keep fields `protected` or `private`
	public MyValue(String name, int age, List<String> sonList,
			Map<String, String> parentsMap) {
		this.name = name;
		this.age = age;
		this.sonList = sonList;
		this.parentsMap = parentsMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getSonList() {
		return sonList;
	}

	public void setSonList(List<String> sonList) {
		this.sonList = sonList;
	}

	public Map<String, String> getParentsMap() {
		return parentsMap;
	}

	public void setParentsMap(Map<String, String> parentsMap) {
		this.parentsMap = parentsMap;
	}

}
