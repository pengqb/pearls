package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.CacheType;
import com.vela.iot.common.Exception400;
import com.vela.iot.common.bean.DevInfo;
import com.vela.iot.common.redis.JedisClient;

public class DevInfoHotService implements IDevInfoService {
	JedisClient jedis = JedisClient.getInstance();
	@Override
	public Map<DevInfo, Object> getDevInfo(String id) {
		Map<String, String> map = jedis.hgetAll(CacheType.devInf, id);
		Map<DevInfo, Object> devInf = DevInfo.hashToEnum(map);
		if (null == devInf) {
			throw new Exception400("DEVINF_IS_NULL", String.format(
					"通过id=%sdevInf.", id));
		}
		return devInf;
	}

}
