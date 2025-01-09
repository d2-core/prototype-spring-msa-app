package com.d2.productservice.model.dto;

import com.d2.productservice.model.enums.FileType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFileDto {
	private FileType fileType;
	private String url;
}
