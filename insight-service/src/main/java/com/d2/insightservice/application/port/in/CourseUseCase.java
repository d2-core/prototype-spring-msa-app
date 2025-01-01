package com.d2.insightservice.application.port.in;

import java.util.List;

import com.d2.insightservice.model.domain.CourseStatics;
import com.d2.insightservice.model.enums.InsightSort;

public interface CourseUseCase {
	void addCourseRating(Double rating);

	void addCourseViewCount(Integer viewCount);

	List<Long> getCourseIds(InsightSort insightSort);

	CourseStatics getCourseStatics(Long courseId);

	Double getCourseStaticsRating(Long courseId);
}
