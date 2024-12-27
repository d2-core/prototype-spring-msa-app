package com.d2.productservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.application.port.in.CourseLevelStaticUseCase;
import com.d2.productservice.application.port.out.StaticPort;
import com.d2.productservice.model.domain.CourseLevel;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseLevelCourseLevelStaticService implements CourseLevelStaticUseCase {
	private final StaticPort staticPort;

	@Transactional
	@Override
	public CourseLevel upsertCourseLevel(Long id, String name, String description) {
		return CourseLevel.from(staticPort.upsert(StaticCategory.COURSE_LEVEL, id, name, description));
	}

	@Transactional
	@Override
	public List<MoveOrder> moveCourseLevel(List<MoveOrder> moveOrders) {
		return staticPort.move(moveOrders);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CourseLevel> getCourseLevelList() {
		return staticPort.getStaticList(StaticCategory.COURSE_LEVEL)
			.stream()
			.map(CourseLevel::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public CourseLevel getCourseLevel(Long id) {
		return CourseLevel.from(staticPort.getStatic(id));
	}

}
