package com.vela.iot.tls;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.net.ssl.X509TrustManager;

public class IgnoreVerifySSL implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		System.out.println("paramString:" + authType);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
		String info = null;
		for (X509Certificate certificate : chain) {
			System.out.println("Sign:"
					+ Base64.getEncoder().encodeToString(
							certificate.getSignature()));
			// 获得证书版本
			info = String.valueOf(certificate.getVersion());
			System.out.println("证书版本:" + info);
			// 获得证书序列号
			info = certificate.getSerialNumber().toString(16);
			System.out.println("证书序列号:" + info);
			// 获得证书有效期
			Date beforedate = certificate.getNotBefore();
			info = dateformat.format(beforedate);
			System.out.println("证书生效日期:" + info);
			Date afterdate = certificate.getNotAfter();
			info = dateformat.format(afterdate);
			System.out.println("证书失效日期:" + info);
			// 获得证书主体信息
			info = certificate.getSubjectDN().getName();
			System.out.println("证书拥有者:" + info);
			// 获得证书颁发者信息
			info = certificate.getIssuerDN().getName();
			System.out.println("证书颁发者:" + info);
			// 获得证书签名算法名称
			info = certificate.getSigAlgName();
			System.out.println("证书签名算法:" + info);
		}

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
