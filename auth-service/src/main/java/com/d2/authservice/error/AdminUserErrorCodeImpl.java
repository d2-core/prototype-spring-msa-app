package com.d2.authservice.error;

import org.springframework.http.HttpStatus;

import com.d2.core.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminUserErrorCodeImpl implements ErrorCode {
	EMAIL_OR_PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST.value(), "AUTH-0000", "email or password not matched",
		"입력하신 이메일 또는 비밀번호가 일치하지 않습니다. 다시 확인해주세요."),
	EXIST_EMAIL_OR_PHONE_NUMBER(HttpStatus.BAD_REQUEST.value(), "AUTH-0001", "exist email or phoneNumber",
		"해당 계정은 이미 존재하는 계정입니다."),
	NOT_COMPLETED_PHONE_AUTH(HttpStatus.BAD_REQUEST.value(), "AUTH-0003", "not completed phone auth",
		"핸드폰 인증을 완료해주세요.");

	private final Integer httpCode;
	private final String code;
	private final String reason;
	private final String message;
}
