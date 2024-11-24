package com.d2.core.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeImpl implements ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "G-001", "internal server error",
		"현재 시스템에 일시적인 문제가 발생했습니다. 조금 후 다시 시도해주세요."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "G-003", "unauthorized access", "접근 권한이 없습니다. 로그인 후 다시 시도해주세요."),
	FORBIDDEN(HttpStatus.FORBIDDEN.value(), "G-004", "forbidden resource", "리소스에 접근할 권한이 없습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "G-005", "bad request", "잘못된 요청입니다. 입력 값을 확인해주세요."),
	NOT_FOUND(HttpStatus.NOT_FOUND.value(), "G-006", "resource not found", "요청한 리소스를 찾을 수 없습니다."),
	INVALID(HttpStatus.BAD_REQUEST.value(), "G-007", "invalid value", "유효하지 않은 값이 입력되었습니다. 다시 확인해주세요."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "G-008", "method not allowed", "잘못된 요청입니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "G-009", "unsupported media type",
		"지원되지 않는 미디어 타입입니다."),
	;

	private final Integer httpCode;
	private final String code;
	private final String reason;
	private final String message;
}
