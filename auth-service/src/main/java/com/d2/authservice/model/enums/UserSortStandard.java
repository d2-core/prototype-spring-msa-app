package com.d2.authservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserSortStandard {
	LAST_LOGIN_AT("최근 로그인 기준");
	private final String description;
}
