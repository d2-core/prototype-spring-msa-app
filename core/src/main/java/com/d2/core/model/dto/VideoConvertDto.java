package com.d2.core.model.dto;

import java.io.File;
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
	private String videoUniqueKey;

	private MultipartFile file;

	private File multipartTempInputFile;

	private List<VideoResolution> targetVideoResolutions;
}

