package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LectureType {
	VIDEO("비디오"),

	DOCUMENT("문서");
	private final String description;
}
