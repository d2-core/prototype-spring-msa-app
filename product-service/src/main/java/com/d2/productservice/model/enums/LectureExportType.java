package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LectureExportType {
	NORMAL("일반 강의"),
	PREVIEW("미리보기"),
	PRIVATE("비공개");
	private final String description;

}
