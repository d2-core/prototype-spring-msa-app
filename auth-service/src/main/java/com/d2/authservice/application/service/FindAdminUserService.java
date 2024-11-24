package com.d2.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.FindAdminUserUseCase;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.model.domain.AdminUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FindAdminUserService implements FindAdminUserUseCase {

	private final AdminUserPort adminUserPort;

	@Override
	public AdminUser getAdminUser(Long adminUserId) {
		return AdminUser.from(adminUserPort.getAdminUser(adminUserId));
	}

	@Override
	public List<AdminUser> getAdminUserList() {
		return adminUserPort.getAdminUserList()
			.stream()
			.map(AdminUser::from)
			.toList();
	}
}
