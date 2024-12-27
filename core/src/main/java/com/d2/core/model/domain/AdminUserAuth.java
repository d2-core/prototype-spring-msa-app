package com.d2.core.model.domain;

import com.d2.core.model.enums.AdminUserRole;
import com.d2.core.model.enums.TokenRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserAuth {
	private Long adminUserId;
	private TokenRole tokenRole;
	private AdminUserRole adminUserRole;

	public AdminUserAuth(Long adminUserId) {
		this.adminUserId = adminUserId;
	}
}
