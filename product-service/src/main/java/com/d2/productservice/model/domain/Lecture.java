package com.d2.productservice.model.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.productservice.model.dto.LectureDto;
import com.d2.productservice.model.dto.LectureReferenceDto;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lecture {
	private final Long id;
	private final Long courseId;
	private final VideoStreamDto videoStream;
	private final String title;
	private final String description;
	private final String thumbnailUrl;
	private final LectureType lectureType;
	private final Long order;
	private final List<LectureReferenceDto> lectureReferences;
	private final List<LectureTimelineDto> chapters;
	private final LectureExportType lectureExportType;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public static Lecture from(LectureDto lectureDto) {
		return new Lecture(
			lectureDto.getId(),
			lectureDto.getCourseId(),
			lectureDto.getVideoStreamDto(),
			lectureDto.getTitle(),
			lectureDto.getDescription(),
			lectureDto.getThumbnailImageUrl(),
			lectureDto.getLectureType(),
			lectureDto.getOrder(),
			lectureDto.getLectureReferences(),
			lectureDto.getChapters(),
			lectureDto.getLectureExportType(),
			lectureDto.getCreatedAt(),
			lectureDto.getUpdatedAt()
		);
	}
}
