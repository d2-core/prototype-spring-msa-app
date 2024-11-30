package com.d2.core.remote;

import java.net.URI;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExternalRestTemplate {

	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;

	public ExternalRestTemplate(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.restTemplate = new RestTemplateBuilder()
			.build();
	}

	public String get(String url, Object query) {
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, String.class);

		return response.getBody();
	}

	public String get(String url, HttpHeaders headers, Object query) {
		URI uri = convertUri(url, query);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}

	public String post(String url, Object body) {
		HttpEntity<?> entity = new HttpEntity<>(body);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return response.getBody();
	}

	public String post(String url, HttpHeaders headers, Object body) {
		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return response.getBody();
	}

	public String patch(String url, Object body) {
		HttpEntity<?> entity = new HttpEntity<>(body);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

		return response.getBody();
	}

	public String patch(String url, HttpHeaders headers, Object body) {
		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

		return response.getBody();
	}

	public String delete(String url, Object query) {
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

		return response.getBody();
	}

	public String delete(String url, HttpHeaders headers, Object query) {
		URI uri = convertUri(url, query);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

		return response.getBody();
	}

	private URI convertUri(String url, Object query) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		Map<String, Object> map = objectMapper.convertValue(query, Map.class);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				uriBuilder.queryParam(entry.getKey(), entry.getValue());
			}
		}

		return uriBuilder.build().encode().toUri();
	}
}
