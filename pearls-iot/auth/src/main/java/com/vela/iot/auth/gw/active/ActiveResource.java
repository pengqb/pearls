package com.vela.iot.auth.gw.active;

import static com.vela.iot.common.YamlConf.initBits;
import static com.vela.iot.common.security.SecurityCode.md5Equals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.common.AResource;
import com.vela.iot.common.AsyncMongoDbClient;
import com.vela.iot.common.ESClient;
import com.vela.iot.common.Exception400;
import com.vela.iot.common.InfluxClient;
import com.vela.iot.common.MongoDbClient;
import com.vela.iot.common.Request;
import com.vela.iot.common.bean.DevInfo;
import com.vela.iot.common.bean.Gateway;
import com.vela.iot.common.redis.JedisClient;
import com.vela.iot.common.redis.LettuceClient;
import com.vela.iot.common.service.DevInfoService;
import com.vela.iot.common.service.GatawayService;
import com.vela.iot.common.service.IDevInfoService;
import com.vela.iot.common.service.IGatewayService;

@Api(value = "/auth/gw", tags = "gw", protocols = "https", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
public class ActiveResource extends AResource {
	

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActiveResource.class);

	@ApiOperation(value = "active", httpMethod="POST")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid input")})
	public String execute(
			@ApiParam(value = "param req of the active", required = true) @FormParam("name") Request req) {
		
		String sn = (String)req.getParams().get("sn");
        String pw = (String)req.getParams().get("pw");
        String npw = (String)req.getParams().get("npw");
        Integer infId = (Integer) req.getParams().get("infId");
        Integer ability = (Integer) req.getParams().get("ability");
        String address = (String) req.getParams().get("address");
       
        //TODO 写在代码校验处，所有的pwd采用相同的校验
//        if (!SecurityCode.isValidPwd(npw)) {
//            return errorReturn(status, "02");
//        }
        IGatewayService gwService = new GatawayService();
        Map<Gateway,Object> gw = gwService.getGateway(sn);
        if(!md5Equals((String)gw.get(Gateway.pwd),(String)gw.get(Gateway.slt),pw)){
        	throw new Exception400("SN_OR_PW_INCORRECT", "SN或密码不正确！");
        } 
        
        IDevInfoService devInfoService = new DevInfoService();
        Map<DevInfo,Object> devInfo = devInfoService.getDevInfo(infId.toString());
        gwService.active(gw, npw);
        
        
        
        gwService.updateGateway(gw, infId,ability);
        
        return null;
		
	}

	private void test(Request req) {
		if (initBits.get(0)) {
			LOGGER.info("req={}",req);
		}
		if (initBits.get(1)) {
			JedisClient redis = JedisClient.getInstance();
			redis.set("hashcode", String.valueOf(req.hashCode()));
		}
		if (initBits.get(2)) {
			LettuceClient.set("hashcode", String.valueOf(req.hashCode()));
		}
		if (initBits.get(3)) {
			MongoDbClient.updateData();
		}
		if (initBits.get(4)) {
			MongoDbClient.bulkUpdateData();
		}
		if (initBits.get(5)) {
			AsyncMongoDbClient.updateData();
		}
		if (initBits.get(6)) {
			AsyncMongoDbClient.bulkUpdateData();
		}
		if (initBits.get(7)) {
			InfluxClient.bulkUpdate(req.hashCode());
		}
		if (initBits.get(8)) {
			InfluxClient.singleUpdateByUdp(req.hashCode());
		}
		if (initBits.get(9)) {
			ESClient.syncUpdateDocument(req.hashCode());
		}
		if (initBits.get(10)) {
			ESClient.asyncUpdateDocument(req.hashCode());
		}
		if (initBits.get(11)) {
			ESClient.bulkUpdateDocument(req.hashCode());
		}
	}
}
