package com.vela.iot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class JedisClient {
	private static Logger log = LoggerFactory.getLogger(JedisClient.class);
	
	private static JedisClient redis;
	
	private JedisClient(){
		
	}
	
	public static JedisClient getInstance(){
		if(redis == null){
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

	public void set(String key, String value) {
        try(Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                return;
            }
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("exception is", e);
        } 
    }
	
	public String get(String key) {
		if (null == key) {
			return null;
		}
        try(Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                return null;
            }
            return jedis.get(key);
        }
    }
    
}
