package com.d2.core.adapter.out.move;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.d2.core.model.domain.MoveOrder;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MoveAbleRepositoryImpl implements MoveAbleRepository {

	private final EntityManager entityManager;

	@Override
	public <T extends MoveAble> List<MoveOrder> move(List<MoveOrder> moveOrders, Class<T> type) {
		if (moveOrders.isEmpty()) {
			return Collections.emptyList();
		}

		List<Long> ids = moveOrders.stream()
			.map(MoveOrder::getId)
			.collect(Collectors.toList());

		String entityName = entityManager.getMetamodel()
			.entity(type)
			.getName();

		List<T> entities = entityManager
			.createQuery("SELECT e FROM " + entityName + " e WHERE e.id IN :ids", type)
			.setParameter("ids", ids)
			.getResultList();

		Map<Long, Long> orderMap = moveOrders.stream()
			.collect(Collectors.toMap(MoveOrder::getId, MoveOrder::getOrder));

		entities.forEach(entity -> {
			Long newOrder = orderMap.get(entity.getId());
			if (newOrder != null) {
				entity.move(newOrder);
			}
		});

		entities.forEach(entityManager::merge);
		entityManager.flush();

		return entities.stream()
			.map(e -> MoveOrder.from(e.getId(), e.getOrders()))
			.collect(Collectors.toList());
	}
}
