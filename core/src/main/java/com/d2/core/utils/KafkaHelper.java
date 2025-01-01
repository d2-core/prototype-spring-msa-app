package com.d2.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.header.Headers;

public class KafkaHelper {

	public static Map<String, String> headersToMap(Headers headers) {
		Map<String, String> headersMap = new HashMap<>();
		headers.forEach(header -> {
			String key = header.key();
			String value = new String(header.value(), StandardCharsets.UTF_8);
			headersMap.put(key, value);
		});
		return headersMap;
	}
}
