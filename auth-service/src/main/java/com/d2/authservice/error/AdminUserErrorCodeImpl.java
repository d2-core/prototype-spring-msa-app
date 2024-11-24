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
	EXIST_EMAIL(HttpStatus.BAD_REQUEST.value(), "AUTH-0001", "exist email",
		"해당 이메일은 이미 등록된 이메일입니다. 다른 이메일을 입력해 주세요."),
	EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST.value(), "AUTH-0002", "exist email",
		"이미 등록된 전화번호입니다. 다른 번호를 입력해 주세요."),
	NOT_COMPLETED_PHONE_AUTH(HttpStatus.BAD_REQUEST.value(), "AUTH-0003", "not completed phone auth",
		"핸드폰 인증을 완료해주세요.");

	private final Integer httpCode;
	private final String code;
	private final String reason;
	private final String message;
}
