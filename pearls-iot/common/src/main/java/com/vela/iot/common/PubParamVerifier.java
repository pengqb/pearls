package com.vela.iot.common;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubParamVerifier {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PubParamVerifier.class);
	private static Map<Param, ParamProperty> paramProperties;

	static {
		paramProperties = new EnumMap<Param, ParamProperty>(Param.class) {
			{
				put(Param.a, new ParamProperty(Type.s, 0, 16, false));
				put(Param.v, new ParamProperty(Type.s, 0, 8, false));
				put(Param.t, new ParamProperty(Type.l, 0, 13, false));
				put(Param.k, new ParamProperty(Type.s, 0, 22, false));
				put(Param.l, new ParamProperty(Type.s, 0, 5, true));
				put(Param.s, new ParamProperty(Type.s, 0, 22, false));
			}
		};
	}
	private static PubParamVerifier verifier;

	private PubParamVerifier() {
	}

	public static PubParamVerifier getInstance() {
		if (verifier == null) {
			verifier = new PubParamVerifier();
		}
		return verifier;
	}

	public void verify(Map<Param, Object> params) {
		for (Entry<Param, ParamProperty> entry : paramProperties.entrySet()) {
			String value = (String) params.get(entry.getKey());
			checkParam(entry, value);
			convert(params, entry, value);
		}
	}

	private void checkParam(Entry<Param, ParamProperty> entry, String value) {
		ParamProperty property = entry.getValue();
		if (!property.isOptional() && value == null) {
			throw new Exception400("PARAM_IS_NULL", String.format(
					"参数检查，发现参数 key=%s为空.", entry.getKey()));
		}
		if (value != null) {
			int valueLength = value.length();
			if (property.getMinLength() >= valueLength
					|| property.getMaxLength() < valueLength) {
				throw new Exception400("PARAM_LEN_ERROR", String.format(
						"参数检查，发现参数 key=%s长度为%d,超出了长度范围%d-%d.", entry.getKey(),
						valueLength, property.getMinLength(),
						property.getMaxLength()));
			}
		}
	}

	private void convert(Map<Param, Object> params,
			Entry<Param, ParamProperty> entry, String value) {
		if (value != null) {
			if (Type.i.equals(entry.getValue().getType())) {
				params.put(entry.getKey(), Integer.valueOf(value));
			}
			if (Type.l.equals(entry.getValue().getType())) {
				params.put(entry.getKey(), Long.valueOf(value));
			}
		}
	}

}
