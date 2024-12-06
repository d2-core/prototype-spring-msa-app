package com.d2.authservice.adapter.out.externalsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.d2.authservice.application.port.out.KakaoPort;
import com.d2.authservice.model.dto.KakaoUserInfoDto;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.remote.ExternalRestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoExternalSystem implements KakaoPort {
	@Value("${kakao.rest.key}")
	private String kakaoRestKey;

	@Value("${kakao.login.redirect.url}")
	private String kakaoLoginRedirectUrl;

	@Value("${kakao.login.access-token.url}")
	private String kakaoLoginAccessTokenUrl;

	@Value("${kakao.login.user-info.url}")
	private String kakaoLoginUserInfoUrl;

	private final ExternalRestTemplate externalRestTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public KakaoUserInfoDto getUserInfo(String code) {
		String accessToken = getAccessToken(code);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer %s".formatted(accessToken));
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		String responseBody = externalRestTemplate.post(kakaoLoginUserInfoUrl, headers, new LinkedMultiValueMap<>());

		try {
			return objectMapper.readValue(responseBody, KakaoUserInfoDto.class);
		} catch (JsonProcessingException ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex,
				"url: %s, response: %s".formatted(kakaoLoginAccessTokenUrl, responseBody));
		}
	}

	public String getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoRestKey);
		body.add("redirect_uri", kakaoLoginRedirectUrl);
		body.add("code", code);

		String responseBody = externalRestTemplate.post(kakaoLoginAccessTokenUrl, headers, body);
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(responseBody);
		} catch (JsonProcessingException ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex, "response: %s".formatted(responseBody));
		}

		return jsonNode.get("access_token").asText();
	}
}
