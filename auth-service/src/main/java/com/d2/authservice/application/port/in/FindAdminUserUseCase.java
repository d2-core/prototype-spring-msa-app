package com.d2.authservice.application.port.in;

import java.util.List;

import com.d2.authservice.model.domain.AdminUser;

public interface FindAdminUserUseCase {

	AdminUser getAdminUser(Long adminUserId);

	List<AdminUser> getAdminUserList();
}
