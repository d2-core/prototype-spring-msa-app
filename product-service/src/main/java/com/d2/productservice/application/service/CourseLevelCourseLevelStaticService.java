package com.d2.productservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.adapter.out.move.MoveAbleRepository;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.adapter.out.persistence.statics.StaticJpaEntity;
import com.d2.productservice.adapter.out.persistence.statics.StaticJpaRepository;
import com.d2.productservice.application.port.in.CourseLevelStaticUseCase;
import com.d2.productservice.model.domain.CourseLevel;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseLevelCourseLevelStaticService implements CourseLevelStaticUseCase {
	private final StaticJpaRepository staticJpaRepository;
	private final MoveAbleRepository moveAbleRepository;

	@Transactional
	@Override
	public CourseLevel upsertCourseLevel(Long id, String name, String description) {
		StaticJpaEntity entity;
		if (id == null) {
			entity = staticJpaRepository.save(
				new StaticJpaEntity(
					StaticCategory.COURSE_LEVEL,
					name,
					description
				)
			);
		} else {
			entity = staticJpaRepository.findById(id)
				.orElseThrow(() -> new ApiExceptionImpl(
					ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id)));

			entity = staticJpaRepository.save(
				entity.modify(name, description));

		}
		return CourseLevel.from(entity);
	}

	@Transactional
	@Override
	public List<MoveOrder> moveCourseLevel(List<MoveOrder> moveOrders) {
		return moveAbleRepository.move(moveOrders, StaticJpaEntity.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CourseLevel> getCourseLevelList() {
		return staticJpaRepository.findAll()
			.stream()
			.map(CourseLevel::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public CourseLevel getCourseLevel(Long id) {
		StaticJpaEntity entity = staticJpaRepository.findById(id)
			.orElseThrow(() -> new ApiExceptionImpl(
				ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id)));

		return CourseLevel.from(entity);
	}

}
