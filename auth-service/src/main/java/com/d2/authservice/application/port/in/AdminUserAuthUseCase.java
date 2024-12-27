package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.core.model.enums.AdminUserRole;

public interface AdminUserAuthUseCase {

	AdminUserLogin signup(AdminUserRole adminUserRole, String nickname, String email, String password,
		String phoneNumber, String authCode, Long checkAuthCodeId);

	AdminUserLogin login(String email, String password);
}
