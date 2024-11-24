package com.d2.authservice.remote.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.adapter.out.helper.JwtTokenHelper;
import com.d2.authservice.error.TokenErrorCodeImpl;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.Role;

class JwtTokenHelperAuthTest extends AuthTestConfig {

	@Autowired
	JwtTokenHelper jwtTokenHelper;

	@Test
	@Disabled("Jwt 토큰 - null 요청 검증")
	void validateTokenWithThrow_1() {
		//Given
		String jwtToken = null;

		//When & Then
		ApiExceptionImpl exception = assertThrows(
			ApiExceptionImpl.class,
			() -> jwtTokenHelper.validateTokenWithThrow(jwtToken)
		);

		Assertions.assertEquals(exception.getResult().getCode(), TokenErrorCodeImpl.TOKEN_NOT_FOUND.getCode());
	}

	@Test
	@Disabled("Jwt 토큰 - 빈문자열 요청 검증")
	void validateTokenWithThrow_2() {
		//Given
		String jwtToken = "";

		//When & Then
		ApiExceptionImpl exception = assertThrows(
			ApiExceptionImpl.class,
			() -> jwtTokenHelper.validateTokenWithThrow(jwtToken)
		);

		Assertions.assertEquals(exception.getResult().getCode(), TokenErrorCodeImpl.TOKEN_NOT_FOUND.getCode());
	}

	@Test
	@Disabled("Jwt 토큰 - 유효하지 않은 토큰 검증")
	void validateTokenWithThrow_3() {
		//Given
		String jwt = "efVVNFUl9JRCI6IjEiLCJleHAiOjE3MzIzODgwOTR9.AFY6IV0PGtEtSAMJoNtd4mTxgORDLJvxVrJLor7pPF!!!!!";

		//When & Then
		ApiExceptionImpl exception = assertThrows(
			ApiExceptionImpl.class,
			() -> jwtTokenHelper.validateTokenWithThrow(jwt)
		);

		Assertions.assertEquals(exception.getResult().getCode(), TokenErrorCodeImpl.INVALID_TOKEN.getCode());
	}

	@Test
	@Disabled("Jwt 토큰 - Jwt 규격에 맞지 않은 요청 검증")
	void validateTokenWithThrow_4() {
		//Given
		String jwt = "invalid token";

		//When & Then
		ApiExceptionImpl exception = assertThrows(
			ApiExceptionImpl.class,
			() -> jwtTokenHelper.validateTokenWithThrow(jwt)
		);

		Assertions.assertEquals(exception.getResult().getCode(), TokenErrorCodeImpl.INTERNAL_TOKEN_ERROR.getCode());
	}

	@Test
	@Disabled("Jwt 토큰 - 만료 검증")
	void validateTokenWithThrow_5() {
		Map<String, Object> data = Map.of(
			"id", 1L,
			"role", Role.ADMIN
		);
		TokenDto expiredToken = jwtTokenHelper.issueToken(data, LocalDateTime.now().minusMinutes(1));

		// When & Then
		ApiExceptionImpl exception = assertThrows(
			ApiExceptionImpl.class,
			() -> jwtTokenHelper.validateTokenWithThrow(expiredToken.getToken())
		);

		Assertions.assertEquals(exception.getResult().getCode(), TokenErrorCodeImpl.EXPIRED_TOKEN.getCode());
	}

	@Test
	@Disabled("Jwt 토큰 - 발급 성공 검증")
	void validateTokenWithThrow_6() {
		//Given
		Map<String, Object> data = Map.of(
			"id", 1L,
			"role", Role.ADMIN
		);
		TokenDto token = jwtTokenHelper.issueToken(data, LocalDateTime.now().plusHours(12));

		// When & Then
		Map<String, Object> result = jwtTokenHelper.validateTokenWithThrow(token.getToken());
		Assertions.assertEquals(String.valueOf(result.get("id")), String.valueOf(data.get("id")));
		Assertions.assertEquals(String.valueOf(result.get("role")), String.valueOf(data.get("role")));
	}
}
