package com.d2.productservice.model.domain;

import com.d2.productservice.adapter.out.persistence.statics.StaticJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseLevel {
	private final Long id;
	private final String name;
	private final String description;
	private final Long order;

	public static CourseLevel from(StaticJpaEntity entity) {
		return new CourseLevel(
			entity.getId(),
			entity.getName(),
			entity.getDescription(),
			entity.getOrder()
		);
	}
}