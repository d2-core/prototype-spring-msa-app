package com.d2.productservice.model.dto;

import com.d2.core.model.enums.VideoResolution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscodeInfoDto {
	private VideoResolution resolution;
	private Long size;
}
