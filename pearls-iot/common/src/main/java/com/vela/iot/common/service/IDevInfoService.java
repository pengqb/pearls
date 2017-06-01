package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.bean.DevInfo;

public interface IDevInfoService {
	public Map<DevInfo, Object> getDevInfo(String id);
}
