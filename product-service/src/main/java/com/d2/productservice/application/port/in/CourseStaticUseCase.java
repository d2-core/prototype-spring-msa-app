package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;

public interface CourseStaticUseCase {
	// Course Level
	StaticDto upsertStatic(StaticCategory staticCategory, Long id, String name, String description);

	List<StaticDto> getStaticList(StaticCategory staticCategory);

	StaticDto getStatic(Long id);

	List<MoveOrder> moveStatic(List<MoveOrder> moveOrders);
}
