package com.vela.iot.common;

import com.vela.iot.common.bean.App;
import com.vela.iot.common.bean.DevInfo;
import com.vela.iot.common.bean.Device;
import com.vela.iot.common.bean.Gateway;

public enum CacheType {
	app(App.class), gw(Gateway.class), devInf(DevInfo.class), dev(Device.class);
	Class<?> cls;

	CacheType(Class<?> c) {
		this.cls = c;
	}

	public Class<?> getCls() {
		return cls;
	}

}
