package com.d2.authservice.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.Role;

public interface AdminUserPort {
	Boolean existEmail(String email);

	Boolean existPhoneNumber(String phoneNumber);

	AdminUserDto register(String name, Role role, AdminUserPermission permission, String email, String password,
		String phoneNumber, AdminUserStatus status, LocalDateTime lastLoginAt);

	AdminUserDto getAdminUserByEmailAndPasswordWithThrow(String email, String password);

	List<AdminUserPermission> getAdminUserPermissions(Long adminUserId);
}
