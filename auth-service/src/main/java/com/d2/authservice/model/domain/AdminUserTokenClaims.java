package com.d2.authservice.model.domain;

import java.util.List;

import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.core.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserTokenClaims {
	private final Role role;

	private final Long adminUserId;

	private final List<AdminUserPermission> permissions;

	public static AdminUserTokenClaims from(Role role, Long adminUserId, List<AdminUserPermission> permissions) {
		return new AdminUserTokenClaims(
			role,
			adminUserId,
			permissions
		);
	}
}
