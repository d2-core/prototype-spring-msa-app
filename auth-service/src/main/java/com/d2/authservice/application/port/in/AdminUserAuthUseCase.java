package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.authservice.model.enums.AdminUserRole;

public interface AdminUserAuthUseCase {

	AdminUserLogin signup(AdminUserRole adminUserRole, String nickname, String email, String password,
		String phoneNumber, String authCode);

	AdminUserLogin login(String email, String password);
}
