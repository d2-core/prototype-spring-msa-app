package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CoursePublishState {
	PUBLISH("발행"),
	PRIVATE("미발행");

	private final String description;
}
