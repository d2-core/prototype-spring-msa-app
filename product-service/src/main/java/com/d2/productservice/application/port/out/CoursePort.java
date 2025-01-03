package com.d2.productservice.application.port.out;

import java.util.List;

import com.d2.productservice.model.dto.CourseDto;
import com.d2.productservice.model.enums.CoursePublishState;

public interface CoursePort {

	CourseDto register(Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId, String title,
		String subTitle, String descriptionWithMarkdown, Long courseLevel, List<String> tags, Integer price);

	CourseDto update(Long id, Long teacherId, List<String> thumbnailImageUrls, Long courseCategoryId, String title,
		String subTitle, String descriptionWithMarkdown, Long courseLevel, List<String> tags, Integer price);

	CourseDto update(Long id, CoursePublishState coursePublishState);

	CourseDto update(Long id, List<String> thumbnailImageUrls);

	CourseDto getCourse(Long courseId);

	List<String> getCourseThumbnailImages(Long courseId);

	List<CourseDto> getCourseList();

	List<CourseDto> getTeacherCourseList(Long teacherId);

	void deleteCourse(Long courseId);

}
