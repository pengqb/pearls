package com.vela.iot.active.coap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelaServer extends CoapServer {

	private static Logger LOGGER = LoggerFactory.getLogger(VelaServer.class);
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(
			NetworkConfig.Keys.COAP_PORT);
	private static final int COAP_SECURE_PORT = NetworkConfig.getStandard()
			.getInt(NetworkConfig.Keys.COAP_SECURE_PORT);

	public static void main(String[] args) {
		try {
			startCoap();
			startCoaps();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static void startCoap() {
		VelaServer server = new VelaServer();
		server.addEndpoints(COAP_PORT);
		server.add(new ActiveResource());
		server.start();
	}

	/**
	 * 绑定监听端口
	 */
	private void addEndpoints(int port) {
		LOGGER.info("[开始绑定监听端口:{}", port);
		for (InetAddress addr : EndpointManager.getEndpointManager()
				.getNetworkInterfaces()) {
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr,
						port);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
		LOGGER.info("[结束绑定监听端口:{}", port);
	}

	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private final static String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "target/classes/coapcerts/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "target/classes/coapcerts/trustStore.jks";

	private static void startCoaps() {
		try {
			VelaServer server = new VelaServer();
			server.addEndpoints(COAP_SECURE_PORT);
			server.add(new ActiveResource());
			server.start();

			// Pre-shared secrets
			InMemoryPskStore pskStore = new InMemoryPskStore();
			pskStore.setKey("password", "sesame".getBytes());
			// load the trust store
			KeyStore trustStore = KeyStore.getInstance("JKS");
			InputStream inTrust = new FileInputStream(TRUST_STORE_LOCATION);
			trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());
			inTrust.close();

			// You can load multiple certificates if needed
			Certificate[] trustedCertificates = new Certificate[1];
			trustedCertificates[0] = trustStore.getCertificate("root");

			// load the key store
			KeyStore keyStore = KeyStore.getInstance("JKS");
			InputStream in = new FileInputStream(KEY_STORE_LOCATION);
			keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
			in.close();

			DtlsConnectorConfig.Builder config = new DtlsConnectorConfig.Builder(
					new InetSocketAddress(COAP_SECURE_PORT));
			config.setSupportedCipherSuites(new CipherSuite[] {
					CipherSuite.TLS_PSK_WITH_AES_128_CCM_8,
					CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 });
			config.setPskStore(pskStore);
			config.setIdentity(
					(PrivateKey) keyStore.getKey("server",
							KEY_STORE_PASSWORD.toCharArray()),
					keyStore.getCertificateChain("server"), true);
			config.setTrustStore(trustedCertificates);

			DTLSConnector connector = new DTLSConnector(config.build());

			server.addEndpoint(new CoapEndpoint(connector, NetworkConfig
					.getStandard()));
			server.start();
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}