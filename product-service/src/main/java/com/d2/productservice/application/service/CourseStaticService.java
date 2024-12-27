package com.d2.productservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.application.port.in.CourseStaticUseCase;
import com.d2.productservice.application.port.out.StaticPort;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseStaticService implements CourseStaticUseCase {
	private final StaticPort staticPort;

	@Transactional
	@Override
	public StaticDto upsertStatic(StaticCategory staticCategory, Long id, String name, String description) {
		return staticPort.upsert(staticCategory, id, name, description);
	}

	@Transactional
	@Override
	public List<MoveOrder> moveStatic(List<MoveOrder> moveOrders) {
		return staticPort.move(moveOrders);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StaticDto> getStaticList(StaticCategory staticCategory) {
		return staticPort.getStaticList(staticCategory);
	}

	@Transactional(readOnly = true)
	@Override
	public StaticDto getStatic(Long id) {
		return staticPort.getStatic(id);
	}

}
