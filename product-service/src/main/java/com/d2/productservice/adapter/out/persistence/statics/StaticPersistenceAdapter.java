package com.d2.productservice.adapter.out.persistence.statics;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.d2.core.adapter.out.move.MoveAbleRepository;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.application.port.out.StaticPort;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StaticPersistenceAdapter implements StaticPort {
	private final StaticJpaRepository staticJpaRepository;
	private final MoveAbleRepository moveAbleRepository;
	private final JPQLQueryFactory jpqlQueryFactory;

	@Override
	public StaticDto upsert(StaticCategory staticCategory, Long id, String name, String description) {
		StaticJpaEntity entity;
		if (id == null) {
			Long orders = countAll(staticCategory) + 1;
			entity = staticJpaRepository.save(
				new StaticJpaEntity(
					StaticCategory.COURSE_CATEGORY,
					name,
					description,
					orders
				)
			);
		} else {
			entity = staticJpaRepository.findById(id)
				.orElseThrow(() -> new ApiExceptionImpl(
					ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id)));

			entity = staticJpaRepository.save(
				entity.modify(name, description));

		}
		return StaticDto.from(entity);
	}

	@Override
	public List<MoveOrder> move(List<MoveOrder> moveOrders) {
		return moveAbleRepository.move(moveOrders, StaticJpaEntity.class);
	}

	@Override
	public List<StaticDto> getStaticList(StaticCategory staticCategory) {
		QStaticJpaEntity staticJpaEntity = QStaticJpaEntity.staticJpaEntity;
		return jpqlQueryFactory.selectFrom(staticJpaEntity)
			.where(staticJpaEntity.staticCategory.eq(staticCategory))
			.fetch()
			.stream().map(StaticDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public StaticDto getStatic(Long id) {
		StaticJpaEntity staticJpaEntity = staticJpaRepository.findById(id)
			.orElseThrow(() -> new ApiExceptionImpl(
				ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id)));
		return StaticDto.from(staticJpaEntity);
	}

	public Long countAll(StaticCategory staticCategory) {
		QStaticJpaEntity staticJpaEntity = QStaticJpaEntity.staticJpaEntity;
		return jpqlQueryFactory.select(staticJpaEntity.id)
			.from(staticJpaEntity)
			.where(staticJpaEntity.staticCategory.eq(staticCategory))
			.orderBy(staticJpaEntity.id.desc())
			.fetchFirst();
	}
}
