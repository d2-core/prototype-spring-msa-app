package com.d2.authservice.adapter.out.persistence.adminuser;

import static com.d2.authservice.adapter.out.persistence.adminuser.QAdminUserJpaEntity.*;
import static com.d2.authservice.adapter.out.persistence.adminuser.QAdminUserPermissionJpaEntity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.Role;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminUserPersistenceAdapter implements AdminUserPort {
	private final JPQLQueryFactory queryFactory;
	private final AdminUserJpaRepository adminUserJpaRepository;
	private final AdminUserPermissionJpaRepository adminUserPermissionJpaRepository;

	@Override
	public Boolean existEmail(String email) {
		QAdminUserJpaEntity adminUserJpaEntity = QAdminUserJpaEntity.adminUserJpaEntity;
		Integer fetchOne = queryFactory
			.selectOne()
			.from(adminUserJpaEntity)
			.where(adminUserJpaEntity.email.eq(email))
			.fetchFirst();

		return fetchOne != null;
	}

	@Override
	public Boolean existPhoneNumber(String phoneNumber) {
		QAdminUserJpaEntity adminUserJpaEntity = QAdminUserJpaEntity.adminUserJpaEntity;
		Integer fetchOne = queryFactory
			.selectOne()
			.from(adminUserJpaEntity)
			.where(adminUserJpaEntity.phoneNumber.eq(phoneNumber))
			.fetchFirst();

		return fetchOne != null;
	}

	@Override
	public AdminUserDto register(String name, Role role, AdminUserPermission permission, String email, String password,
		String phoneNumber, AdminUserStatus status, LocalDateTime lastLoginAt) {
		AdminUserJpaEntity adminUser = adminUserJpaRepository.save(new AdminUserJpaEntity(
			role,
			name,
			email,
			password,
			phoneNumber,
			status,
			lastLoginAt
		));

		AdminUserPermissionJpaEntity adminUserPermissionJpaEntity = adminUserPermissionJpaRepository.save(
			new AdminUserPermissionJpaEntity(
				adminUser.getId(),
				permission
			));

		adminUser.addAdminUserPermissionJpaEntity(adminUserPermissionJpaEntity);

		return AdminUserDto.from(adminUser);
	}

	@Override
	public AdminUserDto getAdminUserByEmailAndPasswordWithThrow(String email, String password) {

		AdminUserJpaEntity entity = queryFactory
			.selectFrom(adminUserJpaEntity)
			.leftJoin(adminUserPermissionJpaEntity)
			.on(adminUserPermissionJpaEntity.adminUserId.eq(adminUserJpaEntity.id))
			.fetchJoin()
			.where(adminUserJpaEntity.email.eq(email))
			.where(adminUserJpaEntity.password.eq(password))
			.fetchFirst();
		if (entity != null) {
			return AdminUserDto.from(entity);
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "email: %s, password: %s]".formatted(email, password));
		}
	}

	@Override
	public List<AdminUserPermission> getAdminUserPermissions(Long adminUserId) {
		return queryFactory
			.select(adminUserPermissionJpaEntity.permission)
			.from(adminUserPermissionJpaEntity)
			.where(adminUserPermissionJpaEntity.adminUserId.eq(adminUserId))
			.fetch();
	}
}
