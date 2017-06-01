package com.vela.iot.common.redis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.vela.iot.common.CacheType;

public class JedisClient {
	private static Logger LOGGER = LoggerFactory.getLogger(JedisClient.class);

	private static JedisClient redis;

	private JedisClient() {

	}

	public static JedisClient getInstance() {
		if (redis == null) {
			redis = new JedisClient();
		}
		return redis;
	}

	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void set(final String key, String value) {
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis == null) {
				return;
			}
			jedis.set(key, value);
			LOGGER.info("op=set,key={},value={}", key,value);
		} 
	}

	public String get(final String key) {
		if (null == key) {
			return null;
		}
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis == null) {
				return null;
			}
			String value = jedis.get(key);
			LOGGER.info("op=set,key={},value={}", key,value);
			return value;
		}
	}

	public void hset(CacheType type, String key, String field, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)
				|| StringUtils.isEmpty(value)) {
			return;
		}
		String cache_key = assemble(type, key);
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis != null) {
				jedis.hset(cache_key, field, value);
				LOGGER.info("op=hset,key={},field={},value={}", cache_key,field,value);
			}
		}
	}

	public String hget(CacheType type, String key, String field) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
			return null;
		}
		String cache_key = assemble(type, key);
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis == null) {
				return null;
			}
			String value = jedis.hget(cache_key, field);
			LOGGER.info("op=hget,key={},field={},value={}", cache_key,field,value);
			return value;
		}
	}

	public Map<String, String> hgetAll(CacheType type, String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		String cache_key = assemble(type, key);
		Map<String, String> map = null;
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis != null) {
				map = jedis.hgetAll(cache_key);
				LOGGER.info("op=hgetAll,key={},values={}", cache_key,map);
			}
		}
		return map;
	}

	public List<String> hmget(CacheType type, String key, String[] fields) {
		if (StringUtils.isEmpty(key) || ArrayUtils.isEmpty(fields)) {
			return null;
		}
		String cache_key = assemble(type, key);
		List<String> values = hmget(cache_key, fields);
		LOGGER.info("op=hmget,key={},fields={},values={}", cache_key,fields,values);
		return values;
	}

	private List<String> hmget(final String key, final String... fields) {
		if (key == null) {
			return null;
		}
		List<String> cacheMap = null;
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis != null) {
				cacheMap = jedis.hmget(key, fields);
			}
		}
		return cacheMap;
	}

	public void hmset(CacheType type, String key, Map<String, String> map) {
		if (StringUtils.isEmpty(key) || map == null || map.isEmpty()) {
			return;
		}
		String cache_key = assemble(type, key);
		hmset(cache_key, map);
		LOGGER.info("op=hmset,key={},values={}", cache_key,map);
	}

	private String hmset(final String key, final Map<String, String> hash) {
		if (key == null) {
			return null;
		}
		String result = null;
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis != null) {
				result = jedis.hmset(key, hash);
			}
		}
		return result;
	}

	public String assemble(CacheType type, String key) {
		switch (type) {
		case app:
			key = CacheType.app + "#" + key;
			break;
		default:
			break;
		}
		return key;
	}
}
