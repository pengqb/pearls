package com.vela.iot.common.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;

public class LettuceClient {
	// 同步方案和连接池同步方案，吞吐量<20000，性能不满足要求
	public static RedisAsyncCommands<String, String> asyncCommands;

	public static void set(String key, String value) {
		asyncCommands.set(key, value);
	}

	public static String get(String key) {
		RedisFuture<String> f = asyncCommands.get(key);
		String value;
		try {
			value = f.get(1, TimeUnit.SECONDS);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

//	public static GenericObjectPool<StatefulRedisConnection<String, String>> pool;
//
//	public static void set(String key, String value) {
//		try {
//			StatefulRedisConnection<String, String> connection = pool
//					.borrowObject();
//			connection.sync().set(key, value);
//			pool.returnObject(connection);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static String get(String key) {
//		try {
//			StatefulRedisConnection<String, String> connection = pool
//					.borrowObject();
//			String value = connection.sync().get(key);
//			pool.returnObject(connection);
//			return value;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}
