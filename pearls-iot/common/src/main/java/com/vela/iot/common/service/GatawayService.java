package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.Param;
import com.vela.iot.common.bean.Gateway;
import com.vela.iot.common.security.SecurityCode;
import com.vela.iot.common.security.SecurityCode.SecurityCodeLevel;

public class GatawayService implements IGatewayService {
	IGatewayService hotService = new GatewayHotService();
	
	@Override
	public Map<Gateway, Object> getGateway(String sn) {
		// TODO 和并从冷数据库里取到的数据
		return hotService.getGateway(sn);
	}

	@Override
	public boolean updateGateway(Map<Gateway, Object> gateway,Integer infId,Integer ability) {
		// TODO 和并从冷数据库里取到的数据
		return hotService.updateGateway(gateway, infId, ability);
	}

	@Override
	public boolean active(Map<Gateway, Object> gateway, String npwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String allotServer(Map<Gateway, Object> gateway, String ip,String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveGateway(Map<Gateway, Object> gateway) {
		// TODO Auto-generated method stub
		
	}

	private void createToken(Map<Gateway, Object> gateway) {
		
		gateway.put(Gateway.key, SecurityCode.getUUIDbyLong());
		gateway.put(Gateway.acKey, SecurityCode.getUUIDbyLong());
		gateway.put(Gateway.scrt, SecurityCode.getSecurityCode(16, SecurityCodeLevel.Hard, false));
		gateway.put(Gateway.acScrt, SecurityCode.getSecurityCode(16, SecurityCodeLevel.Hard, false));
	}
	
}
