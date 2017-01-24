package com.vela.iot.common;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxClient {
	static Logger log = LoggerFactory.getLogger(InfluxClient.class);
	public static InfluxDB influxTcpDB;
	public static InfluxDB influxUdpDB;
	public static String DATABASE = "iot";
	public static String UDPDATABASE = "iotudp";
	public static String RetentionPolicy = "autogen";
	private final static int UDP_PORT = 8089;

	public static void bulkUpdate(int hashcode) {
		Point point = Point.measurement("gw").addField("method", "activebulk")
				.addField("hashcode", hashcode).build();
		influxTcpDB.write(DATABASE, RetentionPolicy, point);
	}

	public static void singleUpdateByUdp(int hashcode) {
		influxUdpDB.disableBatch();
		Point point = Point.measurement("gw1").addField("method", "activeudp")
				.addField("hashcode", hashcode).build();
		
		influxUdpDB.write(UDP_PORT, point.lineProtocol());
		
		Query query = new Query("SELECT * FROM gw GROUP BY *", UDPDATABASE);
		QueryResult result = influxUdpDB.query(query);
		System.out.println(result);
	}
}
