package com.d2.productservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.productservice.adapter.out.persistence.lecture.LectureJpaEntity;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureStatus;
import com.d2.productservice.model.enums.LectureType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
	private Long id;
	private Long courseId;
	private String title;
	private String description;
	private Long videoStreamId;
	private String thumbnailImageUrl;
	private LectureType lectureType;
	private Long order;
	private LectureExportType lectureExportType;
	private List<LectureTimelineDto> chapters;
	private List<LectureReferenceDto> lectureReferences;
	private LectureStatus lectureStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static LectureDto from(LectureJpaEntity entity) {
		return new LectureDto(
			entity.getId(),
			entity.getCourseJpaEntity() != null ? entity.getCourseJpaEntity().getId() : null,
			entity.getTitle(),
			entity.getDescription(),
			entity.getVideoSteamJpaEntity() != null ? entity.getVideoSteamJpaEntity().getId() : null,
			entity.getThumbnailImageUrl(),
			entity.getLectureType(),
			entity.getOrderIndex(),
			entity.getLectureExportType(),
			entity.getChapters(),
			entity.getLectureReferences(),
			entity.getLectureStatus(),
			entity.getCreatedAt(),
			entity.getUpdatedAt()
		);
	}
}
