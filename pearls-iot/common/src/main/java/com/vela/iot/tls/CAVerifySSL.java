package com.vela.iot.tls;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CAVerifySSL implements X509TrustManager {

	private Certificate certificate;
	private static CAVerifySSL cAVerifySSL;

	private CAVerifySSL() {
		this.certificate = getCertificate("D:\\var\\keys\\ca.crt");
	}

	public static CAVerifySSL getInstance() {
		if (cAVerifySSL == null) {
			cAVerifySSL = new CAVerifySSL();
		}
		return cAVerifySSL;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws
	 * @throws Exception
	 */
	private Certificate getCertificate(String certificatePath) {
		try {
			CertificateFactory certificateFactory = CertificateFactory
					.getInstance("X.509");
			FileInputStream in = new FileInputStream(certificatePath);

			certificate = certificateFactory.generateCertificate(in);
			if (in != null) {
				in.close();
			}
			return certificate;
		} catch (CertificateException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

//	public TrustManager[] getTrustManagers() {
//		try {
//			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			keyStore.load(null);
//			int index = 0;
//			String certificateAlias = Integer.toString(index++);
//			keyStore.setCertificateEntry(certificateAlias, certificate);
//
//			TrustManagerFactory trustManagerFactory = TrustManagerFactory
//					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//			trustManagerFactory.init(keyStore);
//			return trustManagerFactory.getTrustManagers();
//			// SSLContext sslContext = SSLContext.getInstance("TLS");
//			// sslContext.init(null, trustManagerFactory.getTrustManagers(),
//			// new SecureRandom());
//			// return sslContext;
//		} catch (CertificateException | KeyStoreException | IOException
//				| NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try {
			for (X509Certificate c : chain) {
				certificate.verify(c.getPublicKey());
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
