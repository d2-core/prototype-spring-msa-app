package com.d2.core.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileFormDto {
	private MultipartFile file;

	private String url;
}
