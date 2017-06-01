package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.bean.DevInfo;

public class DevInfoService implements IDevInfoService {
	IDevInfoService hotService = new DevInfoHotService();
	@Override
	public Map<DevInfo, Object> getDevInfo(String id) {
		return hotService.getDevInfo(id);
	}

}
