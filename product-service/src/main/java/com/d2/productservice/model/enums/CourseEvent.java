package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CourseEvent {
	UPSERT("등록, 수정"),
	DELETE("삭제"),
	PUBLISH("발행"),
	PRIVATE("비공개");

	private final String description;
}
