package com.d2.authservice.model.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUser {
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

	public static AdminUser from(AdminUserDto adminUserDto) {
		return new AdminUser(
			adminUserDto.getId(),
			adminUserDto.getRole(),
			adminUserDto.getPermissions(),
			adminUserDto.getName(),
			adminUserDto.getEmail(),
			adminUserDto.getPassword(),
			adminUserDto.getPhoneNumber(),
			adminUserDto.getStatus(),
			adminUserDto.getRegisteredAt(),
			adminUserDto.getRegisteredAt()
		);
	}
}
