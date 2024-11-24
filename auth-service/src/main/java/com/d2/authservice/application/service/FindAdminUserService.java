package com.d2.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.FindAdminUserUseCase;
import com.d2.authservice.model.domain.AdminUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FindAdminUserService implements FindAdminUserUseCase {

	@Override
	public AdminUser getAdminUser(Long adminUserId) {
		return null;
	}

	@Override
	public List<AdminUser> getAdminUserList() {
		return null;
	}
}
