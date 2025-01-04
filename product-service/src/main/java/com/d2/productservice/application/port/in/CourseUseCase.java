package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.dto.FileForm;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.CourseTeacher;
import com.d2.productservice.model.dto.RangeNumberDto;

public interface CourseUseCase {

	Course upsertCourse(Long adminUserId, Long courseId, List<FileForm> thumbnailImages, Long courseCategoryId,
		String title, String subTitle, String descriptionWithMarkdown, Long courseLevelId, List<String> tags,
		Integer price);

	Long deleteCourse(Long courseId);

	Long publishCourse(Long courseId);

	Long unPublishCourse(Long courseId);

	Course getCourse(Long courseId);

	CourseTeacher getCourseTeacher(Long courseId);

	List<Course> getCourseList(String title, List<Long> courseCategories, List<Long> courseLevels,
		RangeNumberDto priceRange, RangeNumberDto durationRange, RangeNumberDto enrolledCountRange);

	List<Course> getCourseList();

}
