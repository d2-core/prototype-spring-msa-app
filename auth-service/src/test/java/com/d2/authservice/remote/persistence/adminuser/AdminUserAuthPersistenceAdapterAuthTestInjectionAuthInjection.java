package com.d2.authservice.remote.persistence.adminuser;

import static com.d2.authservice.adapter.out.persistence.adminuser.QAdminUserJpaEntity.*;
import static com.d2.authservice.adapter.out.persistence.adminuser.QAdminUserPermissionJpaEntity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserJpaEntity;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserJpaRepository;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserPermissionJpaEntity;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserPermissionJpaRepository;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserPersistenceAdapter;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;

class AdminUserAuthPersistenceAdapterAuthTestInjectionAuthInjection extends AuthTestConfig {
	@Autowired
	JPAQueryFactory queryFactory;

	@Autowired
	AdminUserJpaRepository adminUserJpaRepository;

	@Autowired
	AdminUserPermissionJpaRepository adminUserPermissionJpaRepository;

	@Autowired
	AdminUserPersistenceAdapter adminUserPersistenceAdapter;

	@BeforeEach
	void clean() {
		adminUserJpaRepository.deleteAllInBatch();
		adminUserPermissionJpaRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("기존 이메일이 있을때 검증")
	public void existEmail_1() {
		//Given
		String email = "test@naver.com";

		AdminUserJpaEntity adminUserJpaEntity = new AdminUserJpaEntity(Role.ADMIN, "d2", email, "password",
			"01012341234", AdminUserStatus.REGISTERED, LocalDateTime.now());

		//When
		adminUserJpaRepository.save(adminUserJpaEntity);

		//Then
		Boolean result = adminUserPersistenceAdapter.existEmail(email);

		Assertions.assertTrue(result);
	}

	@Test
	@DisplayName("기존 이메일이 없을때 검증")
	public void existEmail_2() {
		//Given
		String email = "test@naver.com";

		// When & Then
		Boolean result = adminUserPersistenceAdapter.existEmail(email);

		Assertions.assertFalse(result);
	}

	@Test
	@DisplayName("기존 핸드폰 번호가 있을때 검증")
	public void existPhoneNumber_1() {
		//Given
		String phoneNumber = "01012341234";

		AdminUserJpaEntity adminUserJpaEntity = new AdminUserJpaEntity(Role.ADMIN, "d2", "test@naver.com", "password",
			phoneNumber, AdminUserStatus.REGISTERED, LocalDateTime.now());

		// When
		adminUserJpaRepository.save(adminUserJpaEntity);

		//Then
		Boolean result = adminUserPersistenceAdapter.existPhoneNumber(phoneNumber);

		Assertions.assertTrue(result);
	}

	@Test
	@DisplayName("기존 핸드폰 번호가 없을때 검증")
	public void existPhoneNumber_2() {
		//Given
		String phoneNumber = "01012341234";

		// When & Then
		Boolean result = adminUserPersistenceAdapter.existPhoneNumber(phoneNumber);

		Assertions.assertFalse(result);
	}

	@Test
	@DisplayName("어드민 유저 저장")
	public void register() {
		//Given
		String name = "d2";
		Role role = Role.ADMIN;
		AdminUserPermission permission = AdminUserPermission.READ;
		String email = "admin@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		AdminUserStatus status = AdminUserStatus.REGISTERED;
		LocalDateTime lastLoginAt = LocalDateTime.now();

		//When
		AdminUserDto adminUserDto = adminUserPersistenceAdapter.register(name, role, permission, email, password,
			phoneNumber, status, lastLoginAt);

		//Then
		AdminUserJpaEntity adminUser = queryFactory
			.selectFrom(adminUserJpaEntity)
			.where(adminUserJpaEntity.id.eq(adminUserDto.getId()))
			.fetchFirst();

		List<AdminUserPermissionJpaEntity> adminUserPermissionList = queryFactory
			.selectFrom(adminUserPermissionJpaEntity)
			.where(adminUserPermissionJpaEntity.adminUserId.eq(adminUserDto.getId()))
			.fetch();

		Assertions.assertEquals(adminUser.getName(), adminUserDto.getName());
		Assertions.assertEquals(adminUser.getRole(), adminUserDto.getRole());
		Assertions.assertEquals(adminUser.getEmail(), adminUserDto.getEmail());
		Assertions.assertEquals(adminUser.getPassword(), adminUserDto.getPassword());
		Assertions.assertEquals(adminUser.getPhoneNumber(), adminUserDto.getPhoneNumber());
		Assertions.assertEquals(adminUser.getStatus(), adminUserDto.getStatus());
		Assertions.assertEquals(adminUser.getLastLoginAt(), adminUserDto.getLastLoginAt());

		Assertions.assertEquals(adminUserPermissionList.size(), adminUserDto.getPermissions().size());
	}

	@Test
	@DisplayName("이메일, 핸드폰 번호 어드민 유저 가져오기 성공 검증")
	public void getAdminUserByEmailAndPasswordWithThrow_1() {
		//Given
		String name = "d2";
		Role role = Role.ADMIN;
		AdminUserPermission permission = AdminUserPermission.READ;
		String email = "admin@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		AdminUserStatus status = AdminUserStatus.REGISTERED;
		LocalDateTime lastLoginAt = LocalDateTime.now();

		//When
		AdminUserDto adminUserDto = adminUserPersistenceAdapter.register(name, role, permission, email, password,
			phoneNumber, status, lastLoginAt
		);

		//Then
		AdminUserDto result = adminUserPersistenceAdapter.getAdminUserByEmailAndPasswordWithThrow(email, password);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getEmail(), adminUserDto.getEmail());
		Assertions.assertEquals(result.getRole(), adminUserDto.getRole());
		Assertions.assertEquals(result.getPermissions().size(), adminUserDto.getPermissions().size());
	}

	@Test
	@DisplayName("이메일, 핸드폰 번호 어드민 유저 가져오기 실패 검증 - 기존에 등록된 사용자가 없을떄")
	public void getAdminUserByEmailAndPasswordWithThrow_2() {
		//Given
		String email = "d2";
		String password = "password";

		//When
		ApiExceptionImpl exception = Assertions.assertThrows(
			ApiExceptionImpl.class,
			() -> adminUserPersistenceAdapter.getAdminUserByEmailAndPasswordWithThrow(email, password)
		);

		// Then
		Assertions.assertEquals(exception.getResult().getCode(), ErrorCodeImpl.NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("이메일, 핸드폰 번호 어드민 유저 가져오기 실패 검증 - 기존에 등록된 사용자가 있으나 매칭이 되는 이메일이 없을떄")
	public void getAdminUserByEmailAndPasswordWithThrow_3() {
		//Given
		String name = "d2";
		Role role = Role.ADMIN;
		AdminUserPermission permission = AdminUserPermission.READ;
		String email = "admin@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		AdminUserStatus status = AdminUserStatus.REGISTERED;
		LocalDateTime lastLoginAt = LocalDateTime.now();

		//When & Then
		adminUserPersistenceAdapter.register(name, role, permission, email, password, phoneNumber, status, lastLoginAt);
		String inputEmail = "d23";
		ApiExceptionImpl exception = Assertions.assertThrows(
			ApiExceptionImpl.class,
			() -> adminUserPersistenceAdapter.getAdminUserByEmailAndPasswordWithThrow(inputEmail, password)
		);

		Assertions.assertEquals(exception.getResult().getCode(), ErrorCodeImpl.NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("이메일, 핸드폰 번호 어드민 유저 가져오기 실패 검증 - 기존에 등록된 사용자가 있으나 매칭이 되는 패스워드가 없을떄")
	public void getAdminUserByEmailAndPasswordWithThrow_4() {
		//Given
		String name = "d2";
		Role role = Role.ADMIN;
		AdminUserPermission permission = AdminUserPermission.READ;
		String email = "admin@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		AdminUserStatus status = AdminUserStatus.REGISTERED;
		LocalDateTime lastLoginAt = LocalDateTime.now();

		//When & Then
		adminUserPersistenceAdapter.register(name, role, permission, email, password, phoneNumber, status, lastLoginAt);
		String inputPassword = "password__";
		ApiExceptionImpl exception = Assertions.assertThrows(
			ApiExceptionImpl.class,
			() -> adminUserPersistenceAdapter.getAdminUserByEmailAndPasswordWithThrow(email, inputPassword)
		);

		Assertions.assertEquals(exception.getResult().getCode(), ErrorCodeImpl.NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("어드민 유저 권한 가져오기 성공 검증")
	public void getAdminUserPermissions() {
		//Given
		String name = "d2";
		Role role = Role.ADMIN;
		AdminUserPermission permission = AdminUserPermission.READ;
		String email = "admin@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		AdminUserStatus status = AdminUserStatus.REGISTERED;
		LocalDateTime lastLoginAt = LocalDateTime.now();

		//When
		AdminUserDto adminUserDto = adminUserPersistenceAdapter.register(
			name, role, permission, email, password, phoneNumber, status, lastLoginAt);

		// Then
		List<AdminUserPermission> result = adminUserPersistenceAdapter.getAdminUserPermissions(adminUserDto.getId());

		Assertions.assertEquals(result.size(), adminUserDto.getPermissions().size());
	}
}
