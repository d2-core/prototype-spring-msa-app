package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.AdminUserAuthUseCase;
import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.authservice.model.request.AdminUserLoginRequest;
import com.d2.authservice.model.request.AdminUserRegisterRequest;
import com.d2.core.api.API;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AdminUserAuthController {
	private final AdminUserAuthUseCase adminUserAuthUseCase;

	@PostMapping("auth/v1/admin-users")
	public API<AdminUserLogin> signup(@Valid @RequestBody AdminUserRegisterRequest adminUserRegisterRequest
	) {
		return API.OK(
			adminUserAuthUseCase.signup(adminUserRegisterRequest.getAdminUserRole(),
				adminUserRegisterRequest.getNickname(), adminUserRegisterRequest.getEmail(),
				adminUserRegisterRequest.getPassword(), adminUserRegisterRequest.getPhoneNumber(),
				adminUserRegisterRequest.getAuthCode()));

	}

	@PostMapping("auth/v1/admin-users/login")
	public API<AdminUserLogin> login(@Valid @RequestBody AdminUserLoginRequest adminUserLoginRequest) {
		return API.OK(
			adminUserAuthUseCase.login(adminUserLoginRequest.getEmail(), adminUserLoginRequest.getPassword()));
	}
}
