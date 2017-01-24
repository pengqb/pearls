package com.vela.iot.active.netty.http;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.mongodb.MongoClient;
import com.vela.iot.common.AsyncMongoDbClient;
import com.vela.iot.common.ESClient;
import com.vela.iot.common.InfluxClient;
import com.vela.iot.common.JedisClient;
import com.vela.iot.common.LettuceClient;
import com.vela.iot.common.MongoDbClient;
import com.vela.iot.common.YamlConf;

public class ActiveConf {
	static Logger log = LoggerFactory.getLogger(ActiveConf.class);

	public void initSystem() throws FileNotFoundException {
		YamlConf conf = YamlConf.getInstance();
		Map<String, Object> confMap = conf.parse("auth.yml");

		if (ActiveResource.bits.get(1) || ActiveResource.bits.get(2)) {
			Map<String, Object> redisConf = (Map<String, Object>) confMap
					.get("redis");
			initRedis(redisConf);
			initLettuce(redisConf);
		}
		
		if (ActiveResource.bits.get(3) || ActiveResource.bits.get(4)
				|| ActiveResource.bits.get(5) || ActiveResource.bits.get(6)) {
			Map<String, Object> mongoConf = (Map<String, Object>) confMap
					.get("mongo");
			initMongo(mongoConf);
		}

		if (ActiveResource.bits.get(7) || ActiveResource.bits.get(8)) {
			Map<String, Object> influxConf = (Map<String, Object>) confMap
					.get("influx");
			initInfluxDb(influxConf);
		}

		if (ActiveResource.bits.get(9) || ActiveResource.bits.get(10) || ActiveResource.bits.get(11)) {
			Map<String, Object> esConf = (Map<String, Object>) confMap
					.get("es");
			initElasticSearch(esConf);
		}
	}

	private void initServer(Map<String, Object> redisConf) {

	}

	private void initRedis(Map<String, Object> redisConf) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal((Integer) redisConf.get("maxTotal"));
		config.setMaxIdle((Integer) redisConf.get("maxIdle"));
		config.setMinIdle((Integer) redisConf.get("minIdle"));
		String host = (String) redisConf.get("host");
		Integer port = (Integer) redisConf.get("port");
		String password = (String) redisConf.get("password");
		JedisClient redis = JedisClient.getInstance();
		JedisPool jedisPool = new JedisPool(config, host, port, 0, password, 0);
		redis.setJedisPool(jedisPool);
	}

	private void initLettuce(Map<String, Object> redisConf) {
		String host = (String) redisConf.get("host");
		Integer port = (Integer) redisConf.get("port");
		String password = (String) redisConf.get("password");
		com.lambdaworks.redis.RedisClient redisClient = com.lambdaworks.redis.RedisClient
				.create("redis://" + password + "@" + host + ":" + port + "/0");
		StatefulRedisConnection<String, String> connection = redisClient
				.connect();
		// LettuceClient.syncCommands = connection.sync();
		LettuceClient.asyncCommands = connection.async();
		// connection.close();
		// redisClient.shutdown();

		// GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		// config.setMaxTotal((Integer) redisConf.get("maxTotal"));
		// config.setMaxIdle((Integer) redisConf.get("maxIdle"));
		// config.setMinIdle((Integer) redisConf.get("minIdle"));
		// LettuceClient.pool = ConnectionPoolSupport
		// .createGenericObjectPool(() -> redisClient.connect(), config);

	}

	private void initMongo(Map<String, Object> mongoConf) {
		String host = (String) mongoConf.get("host");
		Integer port = (Integer) mongoConf.get("port");
		String name = (String) mongoConf.get("name");
		MongoDbClient.mongo = new MongoClient(host, port);
		MongoDbClient.database = MongoDbClient.mongo.getDatabase(name);

		AsyncMongoDbClient.mongo = com.mongodb.async.client.MongoClients
				.create("mongodb://" + host + ":" + port);
		AsyncMongoDbClient.database = AsyncMongoDbClient.mongo
				.getDatabase(name);
	}
	
	private void initInfluxDb(Map<String, Object> influxConf) {
		String host = (String) influxConf.get("host");
		Integer port = (Integer) influxConf.get("port");
		InfluxClient.influxTcpDB = InfluxDBFactory.connect("http://" + host + ":"
				+ port, "root", "root");
		InfluxClient.influxTcpDB.setLogLevel(LogLevel.NONE);
		InfluxClient.influxTcpDB.deleteDatabase(InfluxClient.DATABASE);
		InfluxClient.influxTcpDB.createDatabase(InfluxClient.DATABASE);
		InfluxClient.influxTcpDB.enableBatch(10000, 3, TimeUnit.SECONDS);
		
		InfluxClient.influxUdpDB = InfluxDBFactory.connect("http://" + host + ":"
				+ port, "root", "root");
		InfluxClient.influxUdpDB.setLogLevel(LogLevel.NONE);
		InfluxClient.influxUdpDB.deleteDatabase(InfluxClient.UDPDATABASE);
		InfluxClient.influxUdpDB.createDatabase(InfluxClient.UDPDATABASE);
		InfluxClient.influxUdpDB.enableBatch(10000, 3, TimeUnit.SECONDS);
		
	}

	private void initElasticSearch(Map<String, Object> esConf) {
		String host = (String) esConf.get("host");
		Integer port = (Integer) esConf.get("port");
		InetAddress address;
		try {
			address = InetAddress.getByName(host);
			ESClient.client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(
							address, port));
		} catch (UnknownHostException e) {
			log.error("初始化ElasticSearch报错：" + e.getMessage(), e);
		}

		ESClient.bulkProcessor = BulkProcessor
				.builder(ESClient.client, new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long executionId, BulkRequest request) {
						log.debug("bulk request num :{}",
								request.numberOfActions());
					}

					@Override
					public void afterBulk(long executionId,
							BulkRequest request, BulkResponse response) {
						if (response.hasFailures()) {
							log.error("请求部分返回错误：{}",
									response.buildFailureMessage());
						}
					}

					@Override
					public void afterBulk(long executionId,
							BulkRequest request, Throwable failure) {
						log.error("请求发生错误：" + failure.getMessage(), failure);
					}
				}).setBulkActions(2000)
				.setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueMillis(200))
				.setConcurrentRequests(0)
				.setBackoffPolicy(BackoffPolicy.noBackoff()).build();
	}
}
