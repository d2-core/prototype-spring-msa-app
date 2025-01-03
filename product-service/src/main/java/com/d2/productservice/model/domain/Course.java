package com.d2.productservice.model.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.productservice.model.dto.CourseDto;
import com.d2.productservice.model.enums.CoursePublishState;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
	private final Long id;

	private final List<String> thumbnailImageUrls;
	private final Long courseCategoryId;
	private final String title;
	private final String subTitle;
	private final String descriptionWithMarkdown;
	private final Long courseLevelId;
	private final List<String> tags;
	private final CoursePublishState publishState;
	private final Integer price;
	private final Integer studentCount;
	private final Integer duration;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime publishedAt;

	public static Course from(CourseDto courseDto) {
		return new Course(
			courseDto.getId(),
			courseDto.getThumbnailImageUrls(),
			courseDto.getCourseCategory(),
			courseDto.getTitle(),
			courseDto.getSubTitle(),
			courseDto.getDescriptionWithMarkdown(),
			courseDto.getCourseLevelId(),
			courseDto.getTags(),
			courseDto.getPublishState(),
			courseDto.getPrice(),
			0,
			0,
			courseDto.getCreatedAt(),
			courseDto.getUpdatedAt(),
			courseDto.getPublishedAt()
		);
	}
}
