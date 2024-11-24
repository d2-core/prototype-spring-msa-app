package com.d2.authservice.error;

import org.springframework.http.HttpStatus;

import com.d2.core.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsAuthErrorCodeImpl implements ErrorCode {
	INVALID_SMS_AUTH_CODE(HttpStatus.BAD_REQUEST.value(), "AUTH-1000", "", ""),
	TIMEOUT_SMS_AUTH_CODE(HttpStatus.BAD_REQUEST.value(), "AUTH-1001", "", "");

	private final Integer httpCode;
	private final String code;
	private final String reason;
	private final String message;
}
