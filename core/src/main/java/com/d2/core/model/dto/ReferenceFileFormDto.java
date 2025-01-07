package com.d2.core.model.dto;

import org.springframework.web.multipart.MultipartFile;

import com.d2.core.model.enums.ReferenceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceFileFormDto {
	private ReferenceType type;

	private String description;

	private String url;

	private MultipartFile file;
}
