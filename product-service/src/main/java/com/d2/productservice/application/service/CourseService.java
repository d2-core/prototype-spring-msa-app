package com.d2.productservice.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.d2.core.application.port.out.ObjectStoragePort;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.dto.FileFormDto;
import com.d2.productservice.application.port.in.CourseUseCase;
import com.d2.productservice.application.port.out.CoursePort;
import com.d2.productservice.application.port.out.LecturePort;
import com.d2.productservice.application.port.out.SendPort;
import com.d2.productservice.application.port.out.TeacherPort;
import com.d2.productservice.model.SendCourseEvent;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.CourseTeacher;
import com.d2.productservice.model.domain.Lecture;
import com.d2.productservice.model.dto.CourseDto;
import com.d2.productservice.model.dto.RangeNumberDto;
import com.d2.productservice.model.dto.TeacherDto;
import com.d2.productservice.model.enums.CourseEvent;
import com.d2.productservice.model.enums.CoursePublishState;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService implements CourseUseCase {

	private final ObjectStoragePort objectStoragePort;
	private final CoursePort coursePort;
	private final TeacherPort teacherPort;
	private final SendPort sendPort;
	private final LecturePort lecturePort;

	@Transactional
	@Override
	public Course upsertCourse(Long courseId, Long teacherId, List<FileFormDto> thumbnailImageFiles,
		Long courseCategoryId, String title, String subTitle, String descriptionWithMarkdown, Long courseLevel,
		List<String> tags, Integer price) {

		CourseDto courseDto;
		if (courseId == null) {
			courseDto = coursePort.register(teacherId, List.of("https://"), courseCategoryId, title, subTitle,
				descriptionWithMarkdown, courseLevel, tags, price);

			List<MultipartFile> multipartFiles = thumbnailImageFiles.stream()
				.map(FileFormDto::getFile)
				.filter(Objects::nonNull)
				.collect(
					Collectors.toList());

			List<String> thumbnailImageUrls = objectStoragePort.uploadFiles(multipartFiles);
			courseDto = coursePort.update(courseDto.getId(), thumbnailImageUrls);

		} else {
			List<String> newImages = thumbnailImageFiles.stream().map(fileForm -> {
				if (fileForm.getFile() == null) {
					return fileForm.getUrl();
				} else {
					return objectStoragePort.uploadFile(fileForm.getFile());
				}
			}).collect(Collectors.toList());

			courseDto = coursePort.update(courseId, teacherId, newImages, courseCategoryId, title,
				subTitle, descriptionWithMarkdown, courseLevel, tags, price);
		}

		try {
			sendPort.sendCourseEvent(new SendCourseEvent(courseId, CourseEvent.UPSERT));
		} catch (Exception e) {
			objectStoragePort.deleteFiles(courseDto.getThumbnailImageUrls());
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}

		return Course.from(courseDto);
	}

	@Transactional
	@Override
	public Long deleteCourse(Long courseId) {
		List<String> fileUrls = coursePort.getFileUrls(courseId);
		
		coursePort.deleteCourse(courseId);

		objectStoragePort.deleteFiles(fileUrls);

		sendPort.sendCourseEvent(new SendCourseEvent(courseId, CourseEvent.DELETE));
		return courseId;
	}

	@Transactional
	@Override
	public Long publishCourse(Long courseId) {
		coursePort.update(courseId, CoursePublishState.PUBLISH);
		sendPort.sendCourseEvent(new SendCourseEvent(courseId, CourseEvent.PUBLISH));
		return courseId;
	}

	@Transactional
	@Override
	public Long unPublishCourse(Long courseId) {
		coursePort.update(courseId, CoursePublishState.PRIVATE);
		sendPort.sendCourseEvent(new SendCourseEvent(courseId, CourseEvent.PRIVATE));
		return courseId;
	}

	@Transactional(readOnly = true)
	@Override
	public Course getCourse(Long courseId) {
		CourseDto courseDto = coursePort.getCourse(courseId);
		return Course.from(courseDto);
	}

	@Transactional(readOnly = true)
	@Override
	public CourseTeacher getCourseTeacher(Long courseId) {
		TeacherDto teacherDto = teacherPort.getTeacher(courseId);
		return CourseTeacher.from(courseId, teacherDto);
	}

	@Override
	public List<Lecture> getCourseLectureList(Long courseId) {
		return lecturePort.getLectureList(courseId)
			.stream().map(Lecture::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Course> getCourseList(String title, List<Long> courseCategories, List<Long> courseLevels,
		RangeNumberDto priceRange, RangeNumberDto durationRange, RangeNumberDto enrolledCountRange) {
		return coursePort.getCourseList()
			.stream().map(Course::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Course> getCourseList() {
		return coursePort.getCourseList()
			.stream().map(Course::from)
			.collect(Collectors.toList());
	}
}
