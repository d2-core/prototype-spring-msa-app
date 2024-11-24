package com.d2.authservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SmsAuthenticationCategory {
	ADMIN_USER_AUTH_SMS("어드민 사용자 문자 인증"),
	USER_AUTH_SMS("일반 사용자 문자 인증");
	private final String description;
}
