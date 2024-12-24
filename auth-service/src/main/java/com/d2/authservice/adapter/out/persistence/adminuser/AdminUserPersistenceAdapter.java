package com.d2.authservice.adapter.out.persistence.adminuser;

import static com.d2.authservice.adapter.out.persistence.adminuser.QAdminUserJpaEntity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserRole;
import com.d2.authservice.model.enums.AdminUserSortStandard;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.constant.PagingConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.dto.SortDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminUserPersistenceAdapter implements AdminUserPort {
	private final JPQLQueryFactory queryFactory;
	private final AdminUserJpaRepository adminUserJpaRepository;

	public Boolean existEmailOrPhoneNumber(String email, String phoneNumber) {
		QAdminUserJpaEntity adminUserJpaEntity = QAdminUserJpaEntity.adminUserJpaEntity;
		Integer fetchOne = queryFactory
			.selectOne()
			.from(adminUserJpaEntity)
			.where(adminUserJpaEntity.email.eq(email).or(adminUserJpaEntity.phoneNumber.eq(phoneNumber)))
			.fetchOne();

		return fetchOne != null;
	}

	@Override
	public AdminUserDto register(AdminUserRole adminUserRole, String nickname, String email, String password,
		String phoneNumber, AdminUserStatus status, LocalDateTime lastLoginAt) {
		AdminUserJpaEntity adminUser = adminUserJpaRepository.save(new AdminUserJpaEntity(
			adminUserRole,
			nickname,
			email,
			password,
			phoneNumber,
			status,
			lastLoginAt
		));

		return AdminUserDto.from(adminUser);
	}

	@Override
	public AdminUserDto getAdminUserByEmailAndPasswordWithThrow(String email, String password) {
		AdminUserJpaEntity entity = queryFactory
			.selectFrom(adminUserJpaEntity)
			.where(adminUserJpaEntity.email.eq(email), adminUserJpaEntity.password.eq(password))
			.fetchFirst();
		if (entity != null) {
			return AdminUserDto.from(entity);
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "email: %s, password: %s]".formatted(email, password));
		}
	}

	@Override
	public AdminUserDto getAdminUser(Long id) {
		AdminUserJpaEntity adminUser = queryFactory
			.selectFrom(adminUserJpaEntity)
			.where(adminUserJpaEntity.id.eq(id))
			.fetchFirst();
		if (adminUser != null) {
			return AdminUserDto.from(adminUser);
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "id: %s".formatted(id));
		}
	}

	@Override
	public List<AdminUserDto> getAdminUserList(String email, String name, String phoneNumber,
		List<SortDto<AdminUserSortStandard>> sorts, Long pageNo, Integer pageSize) {
		BooleanBuilder builder = createUserListSearchBuilder(email, name, phoneNumber);
		OrderSpecifier<?>[] orderSpecifiers = createOrderSpecifiers(sorts);

		if (!pageSize.equals(PagingConstant.ALL)) {
			List<Long> ids = queryFactory
				.select(adminUserJpaEntity.id)
				.from(adminUserJpaEntity)
				.where(builder)
				.orderBy(orderSpecifiers)
				.limit(pageSize)
				.offset(pageNo * pageSize)
				.fetch();

			if (ids.isEmpty()) {
				return List.of();
			}

			return queryFactory
				.selectFrom(adminUserJpaEntity)
				.where(adminUserJpaEntity.id.in(ids))
				.fetch()
				.stream()
				.map(AdminUserDto::from)
				.toList();

		} else {
			return queryFactory
				.selectFrom(adminUserJpaEntity)
				.orderBy(orderSpecifiers)
				.fetch()
				.stream()
				.map(AdminUserDto::from)
				.toList();
		}
	}

	public BooleanBuilder createUserListSearchBuilder(String email, String nickname, String phoneNumber) {
		BooleanBuilder builder = new BooleanBuilder();
		if (!StringUtils.isNullOrEmpty(email)) {
			builder.and(adminUserJpaEntity.email.containsIgnoreCase(email));
		}

		if (!StringUtils.isNullOrEmpty(nickname)) {
			builder.and(adminUserJpaEntity.nickname.containsIgnoreCase(nickname));
		}

		if (!StringUtils.isNullOrEmpty(phoneNumber)) {
			builder.and(adminUserJpaEntity.phoneNumber.containsIgnoreCase(phoneNumber));
		}
		return builder;
	}

	public OrderSpecifier<?>[] createOrderSpecifiers(List<SortDto<AdminUserSortStandard>> sorts) {
		if (sorts == null || sorts.isEmpty()) {
			return new OrderSpecifier[] {adminUserJpaEntity.id.desc()};
		}
		return sorts.stream()
			.map(sort -> {
				AdminUserSortStandard standard = sort.getStandard();
				Order order = sort.getOrderBy().equals(SortDto.Order.DESC) ? Order.DESC : Order.ASC;

				if (standard.equals(AdminUserSortStandard.LAST_LOGIN_AT)) {
					return new OrderSpecifier(order, adminUserJpaEntity.lastLoginAt);
				}
				return new OrderSpecifier(order, adminUserJpaEntity.id);
			})
			.toArray(OrderSpecifier[]::new);
	}
}
