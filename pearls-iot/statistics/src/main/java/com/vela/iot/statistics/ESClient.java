package com.vela.iot.statistics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESClient {
	static Logger log = LoggerFactory.getLogger(ESClient.class);
	public static TransportClient client;
	public static BulkProcessor bulkProcessor;
	private final static int COUNT = 100000;
	public static void init() {
		String host = "127.0.0.1";
		Integer port = 9300;
		InetAddress address;
		try {
			address = InetAddress.getByName(host);
			ESClient.client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(
							address, port));
		} catch (UnknownHostException e) {
			log.error("初始化ElasticSearch报错："+ e.getMessage(), e);
		}
		
		bulkProcessor = BulkProcessor.builder(
		        client,  
		        new BulkProcessor.Listener() {
		            @Override
		            public void beforeBulk(long executionId,
		                                   BulkRequest request) {
		            	log.debug("bulk request num :{}",request.numberOfActions());
		            } 

		            @Override
		            public void afterBulk(long executionId,
		                                  BulkRequest request,
		                                  BulkResponse response) {
		            	if(response.hasFailures()){
		            		log.error("请求部分返回错误：{}", response.buildFailureMessage());
		            	}
		            } 

		            @Override
		            public void afterBulk(long executionId,
		                                  BulkRequest request,
		                                  Throwable failure) {
		            	log.error("请求发生错误："+failure.getMessage(), failure);
		            } 
		        })
		        .setBulkActions(2000) 
		        .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB)) 
		        .setFlushInterval(TimeValue.timeValueMillis(200)) 
		        .setConcurrentRequests(0) 
		        .setBackoffPolicy(BackoffPolicy.noBackoff())
		        .build();
	}
	
	public static void close() throws IOException {
		client.close();
	}
	
	public static void insertDocument() {
		try {
			IndexResponse response = client
					.prepareIndex("iot", "gw", "1")
					.setSource(
							XContentFactory.jsonBuilder().startObject()
									.field("method", "active")
									.field("hashcode", "112233").endObject())
					.get();
		} catch (IOException e) {
			log.error("error:", e);
		}
	}

	public static void syncUpdateDocument() {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("iot");
		updateRequest.type("gw");
		updateRequest.id("1");
		try {
			updateRequest.doc(XContentFactory.jsonBuilder().startObject()
					.field("method", "active1").field("hashcode","112255").endObject());
			long start = System.nanoTime();
			for (int j = 0; j < COUNT; j++) {
				client.update(updateRequest).get();
			}
			System.out.println("sync transport for " + COUNT + " took:" + (System.nanoTime()-start));
		} catch (IOException | ExecutionException | InterruptedException e) {
			log.error("error:", e);
		}
	}
	
	public static void asyncUpdateDocument() {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("iot");
		updateRequest.type("gw");
		updateRequest.id("1");
		try {
			updateRequest.doc(XContentFactory.jsonBuilder().startObject()
					.field("method", "active1").field("hashcode","112255").endObject());
			long start = System.nanoTime();
			for (int j = 0; j < COUNT; j++) {
				client.update(updateRequest);
			}
			System.out.println("async transport for " + COUNT + " took:" + (System.nanoTime()-start));
		} catch (IOException e) {
			log.error("error:", e);
		}
	}
	
	public static void bulkUpdateDocument() {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("iot");
		updateRequest.type("gw");
		updateRequest.id("1");
		try {
			updateRequest.doc(XContentFactory.jsonBuilder().startObject()
					.field("method", "active1").field("hashcode","112255").endObject());
			long start = System.nanoTime();
			for (int j = 0; j < COUNT; j++) {
				bulkProcessor.add(updateRequest);
			}
			// Flush any remaining requests
			bulkProcessor.flush();
			System.out.println("bulk transport for " + COUNT + " took:" + (System.nanoTime()-start));
		} catch (IOException e) {
			log.error("error:", e);
		}
	}

	public static void getDoucment() {
		GetResponse response = client.prepareGet("iot", "gw", "1").get();
		response.toString();
	}

	public static void main(String[] args) {
		ESClient.init();
		try {
			ESClient.syncUpdateDocument();
			//ESClient.asyncUpdateDocument();
			//ESClient.bulkUpdateDocument();
			ESClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
