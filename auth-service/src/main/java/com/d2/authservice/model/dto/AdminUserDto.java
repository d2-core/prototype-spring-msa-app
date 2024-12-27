package com.d2.authservice.model.dto;

import java.time.LocalDateTime;

import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserJpaEntity;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.AdminUserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
	private Long id;

	private AdminUserRole adminUserRole;

	private String nickname;

	private String email;

	private String password;

	private String phoneNumber;

	private AdminUserStatus status;

	private LocalDateTime registeredAt;

	private LocalDateTime lastLoginAt;

	public static AdminUserDto from(AdminUserJpaEntity adminUserJpaEntity) {
		return AdminUserDto.builder()
			.id(adminUserJpaEntity.getId())
			.adminUserRole(adminUserJpaEntity.getAdminUserRole())
			.nickname(adminUserJpaEntity.getNickname())
			.email(adminUserJpaEntity.getEmail())
			.password(adminUserJpaEntity.getPassword())
			.phoneNumber(adminUserJpaEntity.getPhoneNumber())
			.status(adminUserJpaEntity.getStatus())
			.registeredAt(adminUserJpaEntity.getRegisteredAt())
			.lastLoginAt(adminUserJpaEntity.getLastLoginAt())
			.build();
	}
}
