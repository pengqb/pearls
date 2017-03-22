package com.vela.iot.active.netty.http;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

import java.util.BitSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.common.AsyncMongoDbClient;
import com.vela.iot.common.ESClient;
import com.vela.iot.common.InfluxClient;
import com.vela.iot.common.JedisClient;
import com.vela.iot.common.LettuceClient;
import com.vela.iot.common.MongoDbClient;

@Api(value = "/auth/gw", tags = "gw", protocols = "https", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
public class ActiveResource {
	public static BitSet bits = new BitSet(12);

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActiveResource.class);

	@ApiOperation(value = "active", httpMethod="POST")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid input")})
	public String activeAction(
			@ApiParam(value = "param req of the active", required = true) @FormParam("name") String req) {
		if (ActiveResource.bits.get(0)) {
			LOGGER.info("req={}", req);
		}
		if (ActiveResource.bits.get(1)) {
			JedisClient redis = JedisClient.getInstance();
			redis.set("hashcode", String.valueOf(req.hashCode()));
		}
		if (ActiveResource.bits.get(2)) {
			LettuceClient.set("hashcode", String.valueOf(req.hashCode()));
		}
		if (ActiveResource.bits.get(3)) {
			MongoDbClient.updateData();
		}
		if (ActiveResource.bits.get(4)) {
			MongoDbClient.bulkUpdateData();
		}
		if (ActiveResource.bits.get(5)) {
			AsyncMongoDbClient.updateData();
		}
		if (ActiveResource.bits.get(6)) {
			AsyncMongoDbClient.bulkUpdateData();
		}
		if (ActiveResource.bits.get(7)) {
			InfluxClient.bulkUpdate(req.hashCode());
		}
		if (ActiveResource.bits.get(8)) {
			InfluxClient.singleUpdateByUdp(req.hashCode());
		}
		if (ActiveResource.bits.get(9)) {
			ESClient.syncUpdateDocument(req.hashCode());
		}
		if (ActiveResource.bits.get(10)) {
			ESClient.asyncUpdateDocument(req.hashCode());
		}
		if (ActiveResource.bits.get(11)) {
			ESClient.bulkUpdateDocument(req.hashCode());
		}
		return null;
	}
}
