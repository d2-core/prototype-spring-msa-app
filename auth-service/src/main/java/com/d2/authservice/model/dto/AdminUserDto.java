package com.d2.authservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserJpaEntity;
import com.d2.authservice.adapter.out.persistence.adminuser.AdminUserPermissionJpaEntity;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.Role;

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

	private Role role;

	private List<AdminUserPermission> permissions;

	private String name;

	private String email;

	private String password;

	private String phoneNumber;

	private AdminUserStatus status;

	private LocalDateTime registeredAt;

	private LocalDateTime lastLoginAt;

	public static AdminUserDto from(AdminUserJpaEntity adminUserJpaEntity) {
		List<AdminUserPermission> list = adminUserJpaEntity.getPermissions()
			.stream()
			.map(AdminUserPermissionJpaEntity::getPermission)
			.toList();
		return AdminUserDto.builder()
			.id(adminUserJpaEntity.getId())
			.role(adminUserJpaEntity.getRole())
			.permissions(list)
			.name(adminUserJpaEntity.getName())
			.email(adminUserJpaEntity.getEmail())
			.password(adminUserJpaEntity.getPassword())
			.phoneNumber(adminUserJpaEntity.getPhoneNumber())
			.status(adminUserJpaEntity.getStatus())
			.registeredAt(adminUserJpaEntity.getRegisteredAt())
			.lastLoginAt(adminUserJpaEntity.getLastLoginAt())
			.build();
	}
}
