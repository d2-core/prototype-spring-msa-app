package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StaticCategory {
	COURSE_CATEGORY("강의 카테고리"),
	COURSE_LEVEL("강의 레벨"),
	COURSE_RECOMMEND_TAG("강의 추천 태그");
	private final String description;
}
