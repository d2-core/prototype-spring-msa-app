package com.d2.core.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TokenRole {
	ADMIN("ADMIN USER"),
	APP("APP USER");
	private final String description;
}
