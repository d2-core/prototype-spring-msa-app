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

import com.d2.core.api.API;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InternalRestTemplate {

	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;

	public InternalRestTemplate(ObjectMapper objectMapper, AuthInterceptor authInterceptor) {
		this.objectMapper = objectMapper;
		this.restTemplate = new RestTemplateBuilder()
			.interceptors(authInterceptor)
			.build();
	}

	public <T> API<T> get(String url, Object query, TypeReference<API<T>> typeReference) {
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, String.class);

		return validate(uri.toString(), response, typeReference);
	}

	public <T> API<T> get(String url, HttpHeaders headers, Object query, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(headers);
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		return validate(uri.toString(), response, typeReference);
	}

	public <T> API<T> post(String url, Object body, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(body);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return validate(url, response, typeReference);
	}

	public <T> API<T> post(String url, HttpHeaders headers, Object body, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return validate(url, response, typeReference);
	}

	public <T> API<T> patch(String url, Object body, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(body);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

		return validate(url, response, typeReference);
	}

	public <T> API<T> patch(String url, HttpHeaders headers, Object body, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

		return validate(url, response, typeReference);
	}

	public <T> API<T> delete(String url, Object query, TypeReference<API<T>> typeReference) {
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

		return validate(uri.toString(), response, typeReference);
	}

	public <T> API<T> delete(String url, HttpHeaders headers, Object query, TypeReference<API<T>> typeReference) {
		HttpEntity<?> entity = new HttpEntity<>(headers);
		URI uri = convertUri(url, query);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

		return validate(uri.toString(), response, typeReference);
	}

	private URI convertUri(String url, Object query) {
		if (query == null) {
			return URI.create(url);
		}

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		Map<String, Object> map = objectMapper.convertValue(query, new TypeReference<>() {
		});

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				uriBuilder.queryParam(entry.getKey(), entry.getValue());
			}
		}

		return uriBuilder.build().encode().toUri();
	}

	private <T> API<T> validate(String url, ResponseEntity<String> response, TypeReference<API<T>> typeReference) {
		try {
			return objectMapper.readValue(response.getBody(), typeReference);
		} catch (JsonProcessingException ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex,
				"url: %s, response body: %s".formatted(url, response.getBody()));
		}
	}
}
