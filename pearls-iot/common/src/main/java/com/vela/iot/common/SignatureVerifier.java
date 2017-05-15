package com.vela.iot.common;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureVerifier {
	static Logger log = LoggerFactory.getLogger(SignatureVerifier.class);
	
	private String verifySign(Map<String, String> params) {
		String timestamp = params.get("t");
		// 验证时间戳
		if (!checkTimeStamp(Long.valueOf(timestamp))) {
			return "02";
		}
		String signOrig = params.get("s");
		String k = params.get("k");
		try {
			InfoAppKey infoAppKey = infoAppKeyMapper.selectByAppKey(k);
			if (infoAppKey == null || infoAppKey.getEnable() == "N") {
				return "03";
			}
			String secret = infoAppKey.getAppSecret();
			//如果是app或者设备调用接口
			if (timestamp != null) {
				String signVer = createSign(k, secret, timestamp);
				if (!signVer.equals(signOrig)) {
					log.debug("signVer={}signOrig={}" ,signVer, signOrig);
					return "04";
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SignatureVerifier 类的 verifySign 方法出现错误,错误信息是 ", e);
			return "04";
		}
	}

	private boolean checkTimeStamp(Long remoteTimeStamp){
		Date timeStamp = new Date(remoteTimeStamp);
		Long localTime = (new Date()).getTime();
		if (Math.abs(timeStamp.getTime() - localTime) > 1000 * Constant.TIMEOUT_IN_SECONDS) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param key
	 * @param secret
	 * @param timestamp
	 * @return
	 */
	public static String createSign(String key,String secret,String timestamp){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String orgin="key"+key+"timestamp"+timestamp+secret;
			return byte2hex(md.digest(orgin.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 private static String byte2hex(byte[] b) {
	        StringBuffer sign = new StringBuffer();
	        String stmp = "";
	        for (int n = 0; n < b.length; n++) {
	            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
	            if (stmp.length() == 1)
	                sign.append("0").append(stmp);
	            else
	                sign.append(stmp);
	        }
	        return sign.toString().toUpperCase();
	    }
	 
	 public static void main(String args[]){
		 System.out.println(createSign("002FA74E2616F84EFD13A6B0D6C055C0", "0DB44E7652DF51D928E7EF2B923345BF","2016-01-08T16:51:22"));
	 }
}
