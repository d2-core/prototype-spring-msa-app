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
import com.d2.productservice.application.port.in.CourseCategoryStaticUseCase;
import com.d2.productservice.model.domain.CourseCategory;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseCategoryStaticService implements CourseCategoryStaticUseCase {
	private final StaticJpaRepository staticJpaRepository;
	private final MoveAbleRepository moveAbleRepository;

	@Transactional
	@Override
	public CourseCategory upsertCourseCategory(Long id, String name, String description) {

		StaticJpaEntity entity;
		if (id == null) {
			entity = staticJpaRepository.save(
				new StaticJpaEntity(
					StaticCategory.COURSE_CATEGORY,
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
		return CourseCategory.from(entity);

	}

	@Transactional
	@Override
	public List<MoveOrder> moveCourseCategory(List<MoveOrder> moveOrders) {
		return moveAbleRepository.move(moveOrders, StaticJpaEntity.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CourseCategory> getCourseCategoryList() {
		return staticJpaRepository.findAll()
			.stream()
			.map(CourseCategory::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public CourseCategory getCourseCategory(Long id) {
		StaticJpaEntity entity = staticJpaRepository.findById(id)
			.orElseThrow(() -> new ApiExceptionImpl(
				ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id)));

		return CourseCategory.from(entity);
	}
}
