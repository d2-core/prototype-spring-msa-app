package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.dto.FileFormDto;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.CourseTeacher;
import com.d2.productservice.model.domain.Lecture;
import com.d2.productservice.model.dto.RangeNumberDto;

public interface CourseUseCase {

	Course upsertCourse(Long courseId, Long teacherId, List<FileFormDto> thumbnailImages, Long courseCategoryId,
		String title, String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags,
		Integer price);

	Long deleteCourse(Long courseId);

	Long publishCourse(Long courseId);

	Long unPublishCourse(Long courseId);

	Course getCourse(Long courseId);

	CourseTeacher getCourseTeacher(Long courseId);

	List<Lecture> getCourseLectureList(Long courseId);

	List<Course> getCourseList(String title, List<Long> courseCategories, List<Long> courseLevels,
		RangeNumberDto priceRange, RangeNumberDto durationRange, RangeNumberDto enrolledCountRange);

	List<Course> getCourseList();

}
