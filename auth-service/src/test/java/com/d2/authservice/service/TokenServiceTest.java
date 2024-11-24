package com.d2.authservice.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.d2.authservice.AdminUserConstant;
import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.application.service.TokenService;
import com.d2.authservice.model.domain.AdminUserTokenClaims;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.core.model.enums.Role;

class TokenServiceTest extends AuthTestConfig {

	@Mock
	AdminUserPort adminUserPort;

	@Mock
	TokenPort tokenPort;

	@InjectMocks
	TokenService tokenService;

	@Test
	@DisplayName("토큰 검증")
	void validateToken() {
		String jwtToken = "jwtToken";
		Long adminUserId = 1L;
		Map<String, Object> mockData = new HashMap<>();
		mockData.put(AdminUserConstant.ROLE, Role.ADMIN);
		mockData.put(AdminUserConstant.ADMIN_USER_ID, adminUserId);
		mockData.put(AdminUserConstant.ADMIN_USER_PERMISSIONS, List.of(AdminUserPermission.READ));

		List<AdminUserPermission> mockList = List.of(AdminUserPermission.READ);
		when(tokenPort.validateTokenWithThrow(eq(jwtToken))).thenReturn(mockData);
		when(adminUserPort.getAdminUserPermissions(eq(adminUserId))).thenReturn(List.of(AdminUserPermission.READ));

		AdminUserTokenClaims result = tokenService.validateTokenForAdminUser(jwtToken);

		Assertions.assertEquals(result.getAdminUserId().toString(),
			String.valueOf(mockData.get(AdminUserConstant.ADMIN_USER_ID)));
		Assertions.assertEquals(result.getRole().toString(), String.valueOf(mockData.get(AdminUserConstant.ROLE)));
		Assertions.assertEquals(result.getPermissions().size(), mockList.size());
	}

}
