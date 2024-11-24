package com.d2.authservice.error;

import org.springframework.http.HttpStatus;

import com.d2.core.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCodeImpl implements ErrorCode {
	INTERNAL_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "AUTH-2000", "internal token error",
		"예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH-2001", "invalid token",
		"유효하지 않은 토큰입니다. 다시 로그인해주세요."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH-2002", "expired token",
		"접속 시간이 만료되었습니다. 다시 로그인해주세요."),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "AUTH-2003", "token not found",
		"유효하지 않은 토큰입니다. 다시 로그인해주세요.");
	private final Integer httpCode;
	private final String code;
	private final String reason;
	private final String message;
}
