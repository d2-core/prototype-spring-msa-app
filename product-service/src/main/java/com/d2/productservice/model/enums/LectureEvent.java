package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LectureEvent {
	UPSERT("등록"),
	DELETE("삭제"),
	UPDATE_EXPORT_TYPE("표현 업데이트");
	private final String description;
}
