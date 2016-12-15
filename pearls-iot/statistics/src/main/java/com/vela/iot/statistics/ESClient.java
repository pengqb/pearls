package com.vela.iot.statistics;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class ESClient {
	RestClient restClient = null;
		
	public static String ENDPOINT_PRETTY = "/";
	public static Map<String, String> PRETTY = new HashMap<>();
	static {
		PRETTY.put("pretty", "true");
	}

	public void init() {
		if (restClient == null)
			restClient = RestClient
					.builder(new HttpHost("localhost", 9200, "http"))
					.setRequestConfigCallback(
							b -> b.setConnectTimeout(5000).setSocketTimeout(
									30000)).setMaxRetryTimeoutMillis(30000)
					.build();
	}
	

	public void health() throws IOException {
		//"/_cat/nodes"
		Response response = restClient.performRequest("GET", "/_cat/health", Collections.singletonMap("v", "true"));
		System.out.println(EntityUtils.toString(response.getEntity()));
	}

	public void index() throws IOException{
		Response addIndex = restClient.performRequest("PUT", "/customer", Collections.singletonMap("pretty", "true"));
		System.out.println(EntityUtils.toString(addIndex.getEntity()));
		
		Response getIndex = restClient.performRequest("GET", "/_cat/indices", Collections.singletonMap("v", "true"));
		System.out.println(EntityUtils.toString(getIndex.getEntity()));
		
		Response delIndex = restClient.performRequest("DELETE", "/customer", Collections.singletonMap("pretty", "true"));
		System.out.println(EntityUtils.toString(delIndex.getEntity()));
	}
	
	public void document()throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", "John Doe");
		Response addDocument = restClient.performRequest("PUT", "/customer/external/1", map);
		System.out.println(EntityUtils.toString(addDocument.getEntity()));
	}
	
	public void close() throws IOException {
		restClient.close();
	}

	public static void main(String... args) {
		ESClient esc = new ESClient();
		esc.init();
		try {
			esc.document();
			esc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
