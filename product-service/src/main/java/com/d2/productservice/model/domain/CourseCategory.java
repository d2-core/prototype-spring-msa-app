package com.d2.productservice.model.domain;

import com.d2.productservice.adapter.out.persistence.statics.StaticJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseCategory {
	private final Long id;
	private final String name;
	private final String description;
	private final Long order;

	public static CourseCategory from(StaticJpaEntity entity) {
		return new CourseCategory(
			entity.getId(),
			entity.getName(),
			entity.getDescription(),
			entity.getOrder()
		);
	}
}
