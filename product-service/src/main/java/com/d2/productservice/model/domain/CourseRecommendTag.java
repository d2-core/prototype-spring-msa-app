package com.d2.productservice.model.domain;

import com.d2.productservice.model.dto.StaticDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseRecommendTag {
	private final Long id;
	private final String name;
	private final String description;
	private final Long order;

	public static CourseRecommendTag from(StaticDto dto) {
		return new CourseRecommendTag(
			dto.getId(),
			dto.getName(),
			dto.getDescription(),
			dto.getOrders()
		);
	}
}