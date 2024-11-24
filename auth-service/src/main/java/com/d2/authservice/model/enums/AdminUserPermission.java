package com.d2.authservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AdminUserPermission {
	WRITE("읽기 권한"),
	READ("쓰기 권한");
	private final String description;
}
