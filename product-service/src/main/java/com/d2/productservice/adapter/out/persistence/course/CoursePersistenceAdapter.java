package com.d2.productservice.adapter.out.persistence.course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.productservice.application.port.out.CoursePort;
import com.d2.productservice.model.dto.CourseDto;
import com.d2.productservice.model.enums.CoursePublishState;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CoursePersistenceAdapter implements CoursePort {

	private final CourseJpaRepository courseJpaRepository;
	private final JPQLQueryFactory jpqlQueryFactory;

	@Override
	public CourseDto register(Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId, String title,
		String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags, Integer price) {
		CourseJpaEntity courseJpaEntity = courseJpaRepository.save(new CourseJpaEntity(
			teacherId,
			thumbnailImageUrls,
			courseCategoryId,
			title,
			subTitle, descriptionWithMarkdown,
			courseLevelId,
			tags,
			price
		));
		return CourseDto.from(courseJpaEntity);
	}

	@Override
	public CourseDto update(Long courseId, Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId,
		String title, String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags,
		Integer price) {
		CourseJpaEntity courseJpaEntity = getCourseJpaEntity(courseId);
		courseJpaEntity = courseJpaRepository.save(courseJpaEntity.update(
			teacherId,
			thumbnailImageUrls,
			courseCategoryId,
			title,
			subTitle,
			descriptionWithMarkdown,
			courseLevelId,
			tags,
			price
		));

		return CourseDto.from(courseJpaEntity);
	}

	@Override
	public CourseDto update(Long courseId, CoursePublishState coursePublishState) {
		CourseJpaEntity courseJpaEntity = getCourseJpaEntity(courseId);

		CourseJpaEntity newCourseJpaEntity = null;
		if (coursePublishState.equals(CoursePublishState.PUBLISH)) {
			newCourseJpaEntity = courseJpaEntity.update(coursePublishState, LocalDateTime.now());
		}
		if (coursePublishState.equals(CoursePublishState.UN_PUBLISH)) {
			newCourseJpaEntity = courseJpaEntity.update(coursePublishState);
		}

		if (newCourseJpaEntity != null) {
			newCourseJpaEntity = courseJpaRepository.save(newCourseJpaEntity);
			return CourseDto.from(newCourseJpaEntity);
		}

		throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR,
			"coursePublishState: %s".formatted(coursePublishState));
	}

	@Override
	public CourseDto update(Long courseId, List<String> thumbnailImageUrls) {
		CourseJpaEntity courseJpaEntity = getCourseJpaEntity(courseId);
		courseJpaEntity = courseJpaRepository.save(courseJpaEntity.update(thumbnailImageUrls));

		return CourseDto.from(courseJpaEntity);
	}

	@Override
	public CourseDto getCourse(Long courseId) {
		CourseJpaEntity courseJpaEntity = getCourseJpaEntity(courseId);
		return CourseDto.from(courseJpaEntity);
	}

	@Override
	public List<String> getCourseThumbnailImages(Long courseId) {
		QCourseJpaEntity courseJpaEntity = QCourseJpaEntity.courseJpaEntity;
		return jpqlQueryFactory.select(courseJpaEntity.thumbnailImageUrls)
			.from(courseJpaEntity)
			.where(courseJpaEntity.id.eq(courseId))
			.fetchFirst();
	}

	@Override
	public List<CourseDto> getCourseList() {
		return courseJpaRepository.findAll()
			.stream().map(CourseDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public List<CourseDto> getTeacherCourseList(Long teacherId) {
		QCourseJpaEntity courseJpaEntity = QCourseJpaEntity.courseJpaEntity;
		return jpqlQueryFactory.selectFrom(courseJpaEntity)
			.where(courseJpaEntity.teacherId.eq(teacherId))
			.fetch()
			.stream()
			.map(CourseDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteCourse(Long courseId) {
		courseJpaRepository.deleteById(courseId);
	}

	private CourseJpaEntity getCourseJpaEntity(Long courseId) {
		return courseJpaRepository.findById(courseId)
			.orElseThrow(() -> new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(courseId)));
	}
}
