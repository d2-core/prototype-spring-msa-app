package com.d2.core.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VideoResolution {
	P360("해상도 360 지원", 360, 640, 360, 800_000, 96_000),
	P720("해상도 720 지원", 720, 1280, 720, 2_500_000, 128_000),
	P1080("해상도 1024 지원", 1080, 1920, 1080, 4_500_000, 192_000);

	private final String description;
	private final Integer height;
	private final Integer width;
	private final Integer targetHeight;
	private final Integer videoBitrate;
	private final Integer audioBitrate;

	public static List<VideoResolution> getAvailableResolution(Integer originalHeight) {
		return Arrays.stream(VideoResolution.values())
			.filter(resolution -> resolution.height <= originalHeight)
			.collect(Collectors.toList());
	}
}

