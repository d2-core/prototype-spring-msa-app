package com.d2.core.adapter.out.converter;

import com.d2.core.model.dto.OrderImageDto;
import com.fasterxml.jackson.core.type.TypeReference;

public class OrderItemListConverter extends JsonArrayConverter<OrderImageDto> {
	public OrderItemListConverter() {
		super(new TypeReference<>() {
		});
	}
}
