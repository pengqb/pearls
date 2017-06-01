package com.vela.iot.common;

import java.security.MessageDigest;
import java.time.Clock;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vela.iot.common.redis.JedisClient;

public class SignatureVerifier {
	private static SignatureVerifier verifier;

	private SignatureVerifier() {
	}

	public static SignatureVerifier getInstance() {
		if (verifier == null) {
			verifier = new SignatureVerifier();
		}
		return verifier;
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SignatureVerifier.class);

	public String verifySign(Map<Param, Object> params) {
		Long timestamp = (Long) params.get("t");
		// 验证时间戳
		// TODO HTTP和mqtt采用nonce方案，coap同时使用timestamp和nonce方案，
		if (!checkTimeStamp(timestamp)) {
			throw new Exception400("TIMESTAMP_OUT_BOUNDRY", "重放攻击检查，发现时间越界.");
		}
		String signOrig = (String) params.get("s");
		String k = (String) params.get("k");
		try {
			// TODO Jedis 性能没有Lettuce性能好
			List<String> app = JedisClient.getInstance().hmget(CacheType.app,
					k, new String[] { "secret", "enable" });
			if (app == null || app.get(1) == "N") {
				throw new Exception400("APPKEY_IS_ILLEGAL", String.format(
						"appkey非法，key=%s.", k));
			}
			String secret = app.get(0);
			// TODO 签名内容不够
			String signVer = createSign(k, secret, timestamp);
			if (!signVer.equals(signOrig)) {
				throw new Exception400("SIGNATURE_IS_FAULT", String.format(
						"签名错误，signVer=%s,signOrig=%s.", signVer, signOrig));
			}
			return "success";
		} catch (Exception e) {
			throw new Exception400("VERIFYSIGN_IS_FAULT", String.format(
					"SignatureVerifier 类的 verifySign 方法出现错误,错误信息是%s",
					e.getMessage()));
		}
	}

	private boolean checkTimeStamp(Long remoteTimeStamp) {
		Long localTime = Clock.systemUTC().millis();
		if (Math.abs(remoteTimeStamp - localTime) > 1000 * Constant.TIMEOUT_IN_SECONDS) {
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
	public static String createSign(String key, String secret, Long timestamp) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String orgin = "key" + key + "timestamp" + timestamp + secret;
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

	public static void main(String args[]) {
		System.out.println(createSign("002FA74E2616F84EFD13A6B0D6C055C0",
				"0DB44E7652DF51D928E7EF2B923345BF", 1494925608064L));
	}
}
