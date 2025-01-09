package com.d2.productservice.model.dto;

import com.d2.core.model.enums.ReferenceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureReferenceDto {
	private ReferenceType referenceType;
	private String description;
	private String url;
}
