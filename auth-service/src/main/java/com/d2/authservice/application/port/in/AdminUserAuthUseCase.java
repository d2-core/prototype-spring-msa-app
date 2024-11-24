package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.AdminUserLogin;

public interface AdminUserAuthUseCase {

	AdminUserLogin register(String name, String email, String password, String phoneNumber, String authCode);

	AdminUserLogin login(String email, String password);
}
