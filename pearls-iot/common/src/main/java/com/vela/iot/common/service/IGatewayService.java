package com.vela.iot.common.service;

import java.util.Map;

import com.vela.iot.common.bean.Gateway;

public interface IGatewayService {
	public Map<Gateway, Object> getGateway(String sn);
	
	/**
	 * 激活网关，如果有新密码，按新密码激活，并修改密码为新密码，
	 * @param gateway
	 * @param npwd 新密码
	 * @return 激活是否成功，超出激活数，激活不成功。 true 激活成功，false 激活不成功
	 */
	public boolean active(Map<Gateway, Object> gateway,String npwd);

	/**
	 * 修改网关信息
	 * @param gateway
	 * @param params
	 * @return true 有修改，false
	 *         无需修改。每次active接口调用，device的最后访问时间是必须修改的，所以是否有修改不考虑最后访问时间
	 */
	public boolean updateGateway(Map<Gateway, Object> gateway,Integer infId,Integer ability);
	
	/**
	 * 根据ip地址给设备分配服务器地址
	 * @return
	 */
	public String allotServer(Map<Gateway, Object> gateway,String ip,String address);
	
	public void saveGateway(Map<Gateway, Object> gateway);
	
}
