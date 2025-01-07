package com.d2.core.application.port.out;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.d2.core.model.dto.VideoConvertDto;

public interface HlsVideoStoragePort {
	String getExpectedVideoUrl(String videoId);

	String uploadVideo(String videoId, VideoConvertDto videoConvertDto);

	String deleteVideo(String videoUrl);

	default String getVideoId() {
		return LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "/" + UUID.randomUUID();
	}
}
