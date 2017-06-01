package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.CacheType;
import com.vela.iot.common.Exception400;
import com.vela.iot.common.bean.Gateway;
import com.vela.iot.common.redis.JedisClient;

public class GatewayHotService implements IGatewayService {
	JedisClient jedis = JedisClient.getInstance();

	@Override
	public Map<Gateway, Object> getGateway(String sn) {
		Map<String, String> map = jedis.hgetAll(CacheType.gw, sn);
		Map<Gateway, Object> gw = Gateway.hashToEnum(map);
		if (null == gw) {
			throw new Exception400("GW_IS_NULL", String.format(
					"通过sn=%s找不到gateway.", sn));
		}
		return gw;
	}

	@Override
	public boolean updateGateway(Map<Gateway, Object> gateway,Integer infId,Integer ability) {
		if(infId != gateway.get("infId")){
			
		}
		return true;
	}

	@Override
	public boolean active(Map<Gateway, Object> gateway, String npwd) {
		// TODO Auto-generated method stub
		//已经激活过的设备允许直接激活，未超过激活数据，允许激活，激活数量+1，注意并发问题；否则不允许激活
		boolean activeSuccess = true;
		if((Boolean)gateway.get(Gateway.activated)){
			if(npwd == null){
				return activeSuccess;
			}
		}else{
			
		}
		
		return activeSuccess;
	}

	@Override
	public String allotServer(Map<Gateway, Object> gateway, String ip,String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveGateway(Map<Gateway, Object> gateway) {
		// TODO Auto-generated method stub
		//判断激活后的topic_app值与原来的topic_app值(取缓存中的数据)是否相同 1.如果相同则不删除旧的device缓存
        //2.如果不相同则删除旧的device缓存
	}

}
