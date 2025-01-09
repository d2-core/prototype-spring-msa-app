package com.d2.productservice.adapter.out.persistence.course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.core.adapter.out.converter.StringListConverter;
import com.d2.productservice.adapter.out.persistence.lecture.LectureJpaEntity;
import com.d2.productservice.adapter.out.persistence.statics.StaticJpaEntity;
import com.d2.productservice.model.enums.CoursePublishState;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CourseJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long teacherId;

	@Convert(converter = StringListConverter.class)
	@Column(columnDefinition = "text")
	private List<String> thumbnailImageUrls;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_category_id", unique = false)
	private StaticJpaEntity courseCategory;

	@Column(length = 512)
	private String title;

	@Column(length = 1024)
	private String subTitle;

	@Column(columnDefinition = "text")
	private String descriptionWithMarkdown;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_level_id", unique = false)
	private StaticJpaEntity courseLevel;

	@Convert(converter = StringListConverter.class)
	@Column(columnDefinition = "longtext")
	private List<String> tags;

	private Integer price = 0;

	@Column(length = 50, columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private CoursePublishState coursePublishState;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private LocalDateTime publishedAt;

	@OneToMany(mappedBy = "courseJpaEntity", fetch = FetchType.LAZY)
	private List<LectureJpaEntity> lectureJpaEntities = new ArrayList<>();

	public CourseJpaEntity(Long id) {
		this.id = id;
	}

	public CourseJpaEntity(Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId, String title,
		String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags, Integer price) {

		StaticJpaEntity courseCategoryJpaEntity = new StaticJpaEntity(courseCategoryId);
		StaticJpaEntity courseLevelJpaEntity = new StaticJpaEntity(courseLevelId);

		this.teacherId = teacherId;
		this.thumbnailImageUrls = thumbnailImageUrls;
		this.courseCategory = courseCategoryJpaEntity;
		this.title = title;
		this.subTitle = subTitle;
		this.descriptionWithMarkdown = descriptionWithMarkdown;
		this.courseLevel = courseLevelJpaEntity;
		this.tags = tags;
		this.price = price;
		this.coursePublishState = CoursePublishState.PRIVATE;
	}

	public CourseJpaEntity update(Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId, String title,
		String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags, Integer price) {

		StaticJpaEntity courseCategoryJpaEntity = new StaticJpaEntity(courseCategoryId);
		StaticJpaEntity courseLevelJpaEntity = new StaticJpaEntity(courseLevelId);

		this.teacherId = teacherId;
		this.thumbnailImageUrls = thumbnailImageUrls;
		this.courseCategory = courseCategoryJpaEntity;
		this.title = title;
		this.subTitle = subTitle;
		this.descriptionWithMarkdown = descriptionWithMarkdown;
		this.courseLevel = courseLevelJpaEntity;
		this.tags = tags;
		this.price = price;

		return this;
	}

	public CourseJpaEntity update(CoursePublishState coursePublishState, LocalDateTime publishedAt) {
		this.coursePublishState = coursePublishState;
		this.publishedAt = publishedAt;
		return this;
	}

	public CourseJpaEntity update(CoursePublishState coursePublishState) {
		this.coursePublishState = coursePublishState;
		return this;
	}

	public CourseJpaEntity update(List<String> thumbnailImageUrls) {
		this.thumbnailImageUrls = thumbnailImageUrls;
		return this;
	}
}
