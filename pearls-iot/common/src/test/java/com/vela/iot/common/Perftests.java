package com.vela.iot.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.vela.iot.common.bean.GatewayPerfTest;
import com.vela.iot.common.security.SecurityCodePerfTest;

@RunWith(Suite.class)
@SuiteClasses({ GatewayPerfTest.class,SecurityCodePerfTest.class })
public class Perftests {

}
