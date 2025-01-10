package com.d2.productservice.model.domain;

import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.VideoTranscodeStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureVideoCondition {
	private final Long id;

	private final Long lectureId;

	private final String videoUrl;

	private final VideoTranscodeStatus transcodeStatus;

	private final Integer transcodeProgress;

	public static LectureVideoCondition from(VideoStreamDto videoStreamDto) {
		return new LectureVideoCondition(
			videoStreamDto.getId(),
			videoStreamDto.getForeignId(),
			videoStreamDto.getVideoUrl(),
			videoStreamDto.getVideoTranscodeStatus(),
			videoStreamDto.getTranscodeProgress()
		);
	}
}
