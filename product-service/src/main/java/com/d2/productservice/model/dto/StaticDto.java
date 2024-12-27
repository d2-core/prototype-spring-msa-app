package com.d2.productservice.model.dto;

import java.time.LocalDateTime;

import com.d2.productservice.adapter.out.persistence.statics.StaticJpaEntity;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticDto {
	private Long id;

	private StaticCategory staticCategory;

	private String name;

	private String description;

	private Long orders;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public static StaticDto from(StaticJpaEntity entity) {
		return new StaticDto(
			entity.getId(),
			entity.getStaticCategory(),
			entity.getName(),
			entity.getDescription(),
			entity.getOrders(),
			entity.getCreatedAt(),
			entity.getUpdatedAt()
		);
	}
}
