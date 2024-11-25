package com.d2.authservice.adapter.out.externalsystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.d2.authservice.application.port.out.KakaoPort;
import com.d2.authservice.model.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

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

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public KakaoUserInfoDto getUserInfo(String code) throws JsonProcessingException {
		String accessToken = getAccessToken(code);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer %s".formatted(accessToken));
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
			kakaoLoginUserInfoUrl,
			HttpMethod.POST,
			kakaoUserInfoRequest,
			String.class
		);

		String responseBody = response.getBody();

		return objectMapper.readValue(responseBody, KakaoUserInfoDto.class);
	}

	public String getAccessToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoRestKey);
		body.add("redirect_uri", kakaoLoginRedirectUrl);
		body.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(
			kakaoLoginAccessTokenUrl,
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		String responseBody = response.getBody();

		JsonNode jsonNode = objectMapper.readTree(responseBody);

		return jsonNode.get("access_token").asText();
	}
}
