package com.d2.core.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveOrder {
	private final Long id;
	private final Long order;

	public static MoveOrder from(Long id, Long order) {
		return new MoveOrder(id, order);
	}
}
