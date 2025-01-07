package com.d2.core.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.d2.core.model.enums.VideoResolution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoConvertDto {
	private MultipartFile file;

	private List<VideoResolution> targetVideoResolutions;
}
