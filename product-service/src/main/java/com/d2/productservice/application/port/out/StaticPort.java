package com.d2.productservice.application.port.out;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;

public interface StaticPort {

	StaticDto upsert(StaticCategory staticCategory, Long id, String name, String description);

	List<MoveOrder> move(List<MoveOrder> moveOrders);

	List<StaticDto> getStaticList(StaticCategory staticCategory);

	StaticDto getStatic(Long id);
}
