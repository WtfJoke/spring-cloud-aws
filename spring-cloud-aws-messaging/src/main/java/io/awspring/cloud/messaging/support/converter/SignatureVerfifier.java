package io.awspring.cloud.messaging.support.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

public class SignatureVerfifier {

	static boolean verify(JsonNode message, PublicKey key) {
		int version = message.get("SignatureVersion").asInt();
		if (version == 1) {
			String url = message.get("SigningCertURL").asText();
			String signature = message.get("Signature").asText();
			Map<String, Object> keyValues = nodeAsMap(message);
			System.out.println(keyValues);
		}
		return true;
	}

	static PublicKey getKeyFromCert(String url) throws CertificateException, IOException {
		URL destinationURL = new URL(url);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		try (InputStream inStream = destinationURL.openStream()) {
			X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
			return cert.getPublicKey();
		}
	}

	static Map<String, Object> nodeAsMap(JsonNode message){
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(message, new TypeReference<Map<String, Object>>(){});
	}

}
