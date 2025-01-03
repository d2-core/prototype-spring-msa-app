package com.d2.productservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.productservice.adapter.out.persistence.course.CourseJpaEntity;
import com.d2.productservice.model.enums.CoursePublishState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
	private Long id;
	private List<String> thumbnailImageUrls;
	private Long courseCategory;
	private String title;
	private String subTitle;
	private String descriptionWithMarkdown;
	private Long courseLevelId;
	private List<String> tags;
	private Integer price;
	private CoursePublishState publishState;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime publishedAt;

	public static CourseDto from(CourseJpaEntity entity) {
		return new CourseDto(
			entity.getId(),
			entity.getThumbnailImageUrls(),
			entity.getCourseCategory().getId(),
			entity.getTitle(),
			entity.getSubTitle(),
			entity.getDescriptionWithMarkdown(),
			entity.getCourseLevel().getId(),
			entity.getTags(),
			entity.getPrice(),
			entity.getCoursePublishState(),
			entity.getCreatedAt(),
			entity.getUpdatedAt(),
			entity.getPublishedAt()
		);
	}
}
