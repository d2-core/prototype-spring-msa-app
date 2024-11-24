package com.d2.authservice.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.d2.authservice.AdminUserConstant;
import com.d2.authservice.application.port.in.TokenUseCase;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.model.domain.AdminUserTokenClaims;
import com.d2.authservice.model.domain.UserTokenClaims;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.core.model.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService implements TokenUseCase {

	private final AdminUserPort adminUserPort;
	private final TokenPort tokenPort;

	@Override
	public AdminUserTokenClaims validateTokenForAdminUser(String jwtToken) {
		Map<String, Object> data = tokenPort.validateTokenWithThrow(jwtToken);

		Role role = Role.valueOf(data.get(AdminUserConstant.ROLE).toString());
		Long adminUserId = Long.valueOf(data.get(AdminUserConstant.ADMIN_USER_ID).toString());
		List<AdminUserPermission> permissions = adminUserPort.getAdminUserPermissions(adminUserId);

		return AdminUserTokenClaims.from(role, adminUserId, permissions);
	}

	@Override
	public UserTokenClaims validateTokenForUser(String jwtToken) {
		return new UserTokenClaims(1L);
	}
}
