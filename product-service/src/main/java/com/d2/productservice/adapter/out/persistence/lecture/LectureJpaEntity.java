package com.d2.productservice.adapter.out.persistence.lecture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.productservice.adapter.out.persistence.course.CourseJpaEntity;
import com.d2.productservice.adapter.out.persistence.video.VideoSteamJpaEntity;
import com.d2.productservice.model.dto.LectureReferenceDto;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureStatus;
import com.d2.productservice.model.enums.LectureType;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "lectures")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LectureJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_stream_id", referencedColumnName = "id", nullable = true)
	private VideoSteamJpaEntity videoSteamJpaEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private CourseJpaEntity courseJpaEntity;

	@Column(length = 50, columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private LectureType lectureType;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String description;

	@Column(length = 255, nullable = false)
	private String thumbnailImageUrl;

	@Convert(converter = LectureTimelineArrayConverter.class)
	@Column(columnDefinition = "text")
	private List<LectureTimelineDto> chapters = new ArrayList<>();

	@Convert(converter = LectureReferenceArrayConverter.class)
	@Column(columnDefinition = "text")
	private List<LectureReferenceDto> lectureReferences = new ArrayList<>();

	@Column(length = 50, columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private LectureExportType lectureExportType;

	@Column(nullable = false)
	private Long orderIndex;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Column(length = 50, columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private LectureStatus lectureStatus;

	public LectureJpaEntity(Long courseId, LectureType lectureType, String title, String description,
		String thumbnailUrl, List<LectureTimelineDto> chapters, List<LectureReferenceDto> lectureReferences,
		LectureExportType lectureExportType, Long orderIndex, LectureStatus lectureStatus) {

		this.courseJpaEntity = new CourseJpaEntity(courseId);
		this.lectureType = lectureType;
		this.title = title;
		this.description = description;
		this.thumbnailImageUrl = thumbnailUrl;
		this.chapters = chapters;
		this.lectureReferences = lectureReferences;
		this.lectureExportType = lectureExportType;
		this.orderIndex = orderIndex;
		this.lectureStatus = lectureStatus;
	}

	public LectureJpaEntity update(Long courseId, LectureType lectureType, String title, String description,
		String thumbnailUrl, List<LectureTimelineDto> chapters, List<LectureReferenceDto> lectureReferences,
		LectureExportType lectureExportType, Long orderIndex, LectureStatus lectureStatus) {

		this.courseJpaEntity = new CourseJpaEntity(courseId);
		this.lectureType = lectureType;
		this.title = title;
		this.description = description;
		this.thumbnailImageUrl = thumbnailUrl;
		this.chapters = chapters;
		this.lectureReferences = lectureReferences;
		this.lectureExportType = lectureExportType;
		this.orderIndex = orderIndex;
		this.lectureStatus = lectureStatus;

		return this;
	}

	public LectureJpaEntity update(String thumbnailImageUrl, List<LectureReferenceDto> lectureReferences) {
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.lectureReferences = lectureReferences;
		return this;
	}

	public LectureJpaEntity update(LectureExportType lectureExportType) {
		this.lectureExportType = lectureExportType;
		return this;
	}

	public LectureJpaEntity update(Long videoStreamId) {
		this.videoSteamJpaEntity = new VideoSteamJpaEntity(videoStreamId);

		return this;
	}
}
