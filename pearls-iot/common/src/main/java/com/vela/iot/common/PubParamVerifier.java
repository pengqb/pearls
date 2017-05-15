package com.vela.iot.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubParamVerifier {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PubParamVerifier.class);
	private static Map<String, ParamProperty> paramProperties;

	static {
		paramProperties = new HashMap<String, ParamProperty>() {
			{
				put("a", new ParamProperty(0, 16, false));
				put("v", new ParamProperty(0, 8, false));
				put("t", new ParamProperty(0, 13, false));
				put("k", new ParamProperty(0, 22, false));
				put("l", new ParamProperty(0, 5, true));
				put("s", new ParamProperty(0, 22, false));
			}
		};
	}

	public void verify(Map<String, String> params) {
		for(Entry<String,ParamProperty> entry : paramProperties.entrySet()){
			ParamProperty property = entry.getValue();
			String value = params.get(entry.getKey());
			if(!property.isOptional() && value == null){
				LOGGER.error("参数检查，发现参数 key={}为空.",
						entry.getKey());
				throw new Exception400("PARAM_IS_NULL","PARAM_IS_NULL");
			}
			if(value != null){
				int valueLength = value.length();
				if (property.getMinLength() >= valueLength
						|| property.getMaxLength() < valueLength) {
					LOGGER.error("参数检查，发现参数 key={}长度为{},超出了长度范围{}-{}.",
							entry.getKey(), valueLength, property.getMinLength(),
							property.getMaxLength());
					throw new Exception400("PARAM_LEN_ERROR","PARAM_LEN_ERROR");
				}
			}
		}
	}
}
