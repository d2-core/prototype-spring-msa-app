package com.d2.authservice.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.FindAdminUserUseCase;
import com.d2.authservice.model.domain.AdminUser;
import com.d2.core.api.API;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FindAdminUserController {
	private final FindAdminUserUseCase findAdminUserUseCase;

	@GetMapping("auth/v1/admin-users/me")
	public API<AdminUser> getAdminUser() {
		return API.OK(findAdminUserUseCase.getAdminUser(1L));
	}

	@GetMapping("auth/v1/admin-users/{adminUserId}")
	public API<AdminUser> getAdminUserByAdminUserId(@PathVariable Long adminUserId) {
		return API.OK(findAdminUserUseCase.getAdminUser(adminUserId));
	}

	@GetMapping("auth/v1/admin-users")
	public API<List<AdminUser>> getAdminUserList() {
		return API.OK(findAdminUserUseCase.getAdminUserList());
	}
}
