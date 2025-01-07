package com.d2.core.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReferenceType {
	PDF("PDF 파일"),
	LINK("링크");

	private final String description;
}
