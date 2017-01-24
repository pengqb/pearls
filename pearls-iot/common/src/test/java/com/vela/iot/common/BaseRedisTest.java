package com.vela.iot.common;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BaseRedisTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSet() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        //config.setTestOnBorrow(true);
        //config.setTestOnReturn(true);
        //config.setTestWhileIdle(true);
        //config.setTimeBetweenEvictionRunsMillis(20000);
        String host = "192.168.1.133";
        String password = "vane123";
        JedisPool pool = null;
        if ("0".equals(password)) {
            pool = new JedisPool(config, host, 6381, 0, null, 0);
        } else {
            pool = new JedisPool(config, host, 6381, 0, password, 0);
        }
        JedisClient redis = JedisClient.getInstance();        
        redis.setJedisPool(pool);       
        //Pipeline p = redis.pipelined();
        //List<String> keys =new ArrayList<String>();
        //keys.add("DP_BLOB#1_00124B0003CC0EDA_1124097_");	
        redis.set("count", "1");
        System.out.println(redis.get("count"));
        assertEquals("1",redis.get("count"));
        
	}

}
