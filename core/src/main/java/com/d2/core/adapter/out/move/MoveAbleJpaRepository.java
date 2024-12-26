package com.d2.core.adapter.out.move;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.d2.core.model.domain.MoveOrder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MoveAbleJpaRepository<T extends MoveAble> implements MoveAbleRepository<T> {
	private final JpaRepository<T, Long> jpaRepository;

	@Override
	public List<MoveOrder> move(List<MoveOrder> moveOrders) {
		List<Long> ids = moveOrders.stream()
			.map(MoveOrder::getId)
			.collect(Collectors.toList());

		List<T> entities = jpaRepository.findAllById(ids);

		Map<Long, Long> orderMap = moveOrders.stream()
			.collect(Collectors.toMap(MoveOrder::getId, MoveOrder::getOrder));

		entities.forEach(entity -> {
			Long newOrder = orderMap.get(entity.getId());
			if (newOrder != null) {
				entity.move(newOrder);
			}
		});

		return jpaRepository.saveAll(entities)
			.stream()
			.map(e -> MoveOrder.from(e.getId(), e.getOrders()))
			.collect(Collectors.toList());
	}
}
