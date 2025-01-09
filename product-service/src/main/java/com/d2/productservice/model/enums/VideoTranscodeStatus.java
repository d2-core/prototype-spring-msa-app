package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VideoTranscodeStatus {
	CREATE("생성"),
	COMPLETE("완료"),
	PROGRESS("진행중"),
	FAIL("실패");
	private final String description;
}
