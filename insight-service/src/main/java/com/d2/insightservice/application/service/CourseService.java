package com.d2.insightservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.insightservice.application.port.in.CourseUseCase;
import com.d2.insightservice.model.domain.CourseStatics;
import com.d2.insightservice.model.enums.InsightSort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService implements CourseUseCase {

	// kafka
	// redis

	@Override
	public void addCourseRating(Double rating) {
		
	}

	@Override
	public void addCourseViewCount(Integer viewCount) {

	}

	@Override
	public List<Long> getCourseIds(InsightSort insightSort) {
		return null;
	}

	@Override
	public CourseStatics getCourseStatics(Long courseId) {
		return null;
	}

	@Override
	public Double getCourseStaticsRating(Long courseId) {
		return null;
	}
}
