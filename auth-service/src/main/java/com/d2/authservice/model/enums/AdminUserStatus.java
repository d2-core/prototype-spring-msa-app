package com.d2.authservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AdminUserStatus {
	REGISTERED("등록"),
	UNREGISTERED("해지");
	private final String description;
}
