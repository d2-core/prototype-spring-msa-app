package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LectureStatus {
	REGISTER("등록"),
	DELETE("삭제");
	private final String description;
}
