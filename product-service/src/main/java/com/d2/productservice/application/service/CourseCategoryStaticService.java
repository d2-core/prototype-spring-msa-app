package com.d2.productservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.application.port.in.CourseCategoryStaticUseCase;
import com.d2.productservice.application.port.out.StaticPort;
import com.d2.productservice.model.domain.CourseCategory;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseCategoryStaticService implements CourseCategoryStaticUseCase {
	private final StaticPort staticPort;

	@Transactional
	@Override
	public CourseCategory upsertCourseCategory(Long id, String name, String description) {
		return CourseCategory.from(staticPort.upsert(StaticCategory.COURSE_CATEGORY, id, name, description));
	}

	@Transactional
	@Override
	public List<MoveOrder> moveCourseCategory(List<MoveOrder> moveOrders) {
		return staticPort.move(moveOrders);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CourseCategory> getCourseCategoryList() {
		return staticPort.getStaticList(StaticCategory.COURSE_CATEGORY)
			.stream()
			.map(CourseCategory::from)
			.collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	@Override
	public CourseCategory getCourseCategory(Long id) {
		return CourseCategory.from(staticPort.getStatic(id));
	}
}
