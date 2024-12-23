package com.d2.authservice.adapter.out.persistence.user;

import static com.d2.authservice.adapter.out.persistence.user.QUserJpaEntity.*;
import static com.d2.authservice.adapter.out.persistence.user.QUserSocialProfileJpaEntity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.UserPort;
import com.d2.authservice.model.dto.UserDto;
import com.d2.authservice.model.enums.SocialCategory;
import com.d2.authservice.model.enums.UserSortStandard;
import com.d2.authservice.model.enums.UserStatus;
import com.d2.core.constant.PagingConstant;
import com.d2.core.model.dto.SortDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserPersistenceAdapter implements UserPort {
	private final JPAQueryFactory queryFactory;
	private final UserJpaRepository userJpaRepository;
	private final UserSocialProfileJpaRepository userSocialProfileJpaRepository;

	@Override
	public UserDto upsertBySocialProfileId(String socialProfileId, String nickname, String email,
		String phoneNumber,
		UserStatus userStatus,
		LocalDateTime lastLoginAt, SocialCategory socialCategory) {

		UserJpaEntity user = queryFactory
			.selectFrom(userJpaEntity)
			.leftJoin(userSocialProfileJpaEntity, userSocialProfileJpaEntity)
			.fetchJoin()
			.where(userSocialProfileJpaEntity.socialProfileId.eq(socialProfileId))
			.fetchFirst();

		UserJpaEntity userJpaEntity;
		UserSocialProfileJpaEntity userSocialProfileJpaEntity;
		if (user == null) {
			userSocialProfileJpaEntity = new UserSocialProfileJpaEntity(socialProfileId, SocialCategory.KAKAO);
			userJpaEntity = new UserJpaEntity(nickname, email, phoneNumber, userStatus, lastLoginAt);

		} else {
			userSocialProfileJpaEntity = user.getUserSocialProfileJpaEntity().update(socialProfileId, socialCategory);
			userJpaEntity = user.update(nickname, email, phoneNumber, userStatus, lastLoginAt);
		}

		UserSocialProfileJpaEntity savedUserSocialProfile = userSocialProfileJpaRepository.save(
			userSocialProfileJpaEntity);
		UserJpaEntity savedUser = userJpaRepository.save(userJpaEntity);

		savedUser.setUserSocialProfileJpaEntity(savedUserSocialProfile);

		return UserDto.from(savedUser);
	}

	@Override
	public Boolean exitSocialId(String socialProfileId) {
		Integer result = queryFactory.selectOne()
			.from(userSocialProfileJpaEntity)
			.where(userSocialProfileJpaEntity.socialProfileId.eq(socialProfileId))
			.fetchFirst();

		return result != null;
	}

	@Override
	public Boolean existEmail(String email) {
		Integer result = queryFactory
			.selectOne()
			.from(userJpaEntity)
			.where(userJpaEntity.email.eq(email))
			.fetchFirst();

		return result != null;
	}

	@Override
	public Boolean existPhoneNumber(String phoneNumber) {
		Integer result = queryFactory
			.selectOne()
			.from(userJpaEntity)
			.where(userJpaEntity.phoneNumber.eq(phoneNumber))
			.fetchFirst();

		return result != null;
	}

	@Override
	public UserDto getUser(Long id) {
		UserJpaEntity user = queryFactory
			.selectFrom(userJpaEntity)
			.where(userJpaEntity.id.eq(id))
			.fetchFirst();

		return UserDto.from(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		UserJpaEntity user = queryFactory
			.selectFrom(userJpaEntity)
			.where(userJpaEntity.email.eq(email))
			.fetchFirst();

		return UserDto.from(user);
	}

	@Override
	public UserDto getUserByPhoneNumber(String phoneNumber) {
		UserJpaEntity user = queryFactory
			.selectFrom(userJpaEntity)
			.where(userJpaEntity.phoneNumber.eq(phoneNumber))
			.fetchFirst();

		return UserDto.from(user);
	}

	@Override
	public List<UserDto> getUserList(String email, String nickname, String phoneNumber,
		List<SortDto<UserSortStandard>> sorts, Long pageNo, Integer pageSize) {
		BooleanBuilder builder = createUserListSearchBuilder(email, nickname, phoneNumber);
		OrderSpecifier<?>[] orderSpecifiers = createOrderSpecifiers(sorts);

		if (!pageSize.equals(PagingConstant.ALL)) {
			List<Long> ids = queryFactory
				.select(userJpaEntity.id)
				.from(userJpaEntity)
				.where(builder)
				.orderBy(orderSpecifiers)
				.limit(pageSize)
				.offset(pageNo * pageSize)
				.fetch();

			if (ids.isEmpty()) {
				return List.of();
			}

			return queryFactory
				.selectFrom(userJpaEntity)
				.where(userJpaEntity.id.in(ids))
				.fetch()
				.stream()
				.map(UserDto::from)
				.toList();
		} else {
			return queryFactory
				.selectFrom(userJpaEntity)
				.orderBy(orderSpecifiers)
				.fetch()
				.stream()
				.map(UserDto::from)
				.toList();
		}
	}

	public BooleanBuilder createUserListSearchBuilder(String email, String nickname, String phoneNumber) {
		BooleanBuilder builder = new BooleanBuilder();
		if (!StringUtils.isNullOrEmpty(email)) {
			builder.and(userJpaEntity.email.containsIgnoreCase(email));
		}

		if (!StringUtils.isNullOrEmpty(nickname)) {
			builder.and(userJpaEntity.nickname.containsIgnoreCase(nickname));
		}

		if (!StringUtils.isNullOrEmpty(phoneNumber)) {
			builder.and(userJpaEntity.phoneNumber.containsIgnoreCase(phoneNumber));
		}
		return builder;
	}

	public OrderSpecifier<?>[] createOrderSpecifiers(List<SortDto<UserSortStandard>> sorts) {
		if (sorts == null || sorts.isEmpty()) {
			return new OrderSpecifier[] {userJpaEntity.id.desc()};
		}
		return sorts.stream()
			.map(sort -> {
				UserSortStandard standard = sort.getStandard();
				Order order = sort.getOrderBy().equals(SortDto.Order.DESC) ? Order.DESC : Order.ASC;

				if (standard.equals(UserSortStandard.LAST_LOGIN_AT)) {
					return new OrderSpecifier(order, userJpaEntity.lastLoginAt);
				}
				return new OrderSpecifier(order, userJpaEntity.id);
			})
			.toArray(OrderSpecifier[]::new);
	}
}
