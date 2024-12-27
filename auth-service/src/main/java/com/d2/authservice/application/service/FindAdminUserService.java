package com.d2.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.FindAdminUserUseCase;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.model.domain.AdminUser;
import com.d2.authservice.model.dto.AdminUserAutoDto;
import com.d2.authservice.model.enums.AdminUserSortStandard;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.dto.SortDto;
import com.d2.core.model.enums.TokenRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FindAdminUserService implements FindAdminUserUseCase {

	private final AdminUserPort adminUserPort;

	@Override
	public AdminUserAuth getAdminUserAuth(Long adminUserId) {
		AdminUserAutoDto adminUserAutoDto = adminUserPort.getAdminUserAuth(adminUserId);
		return new AdminUserAuth(adminUserAutoDto.getId(), TokenRole.ADMIN, adminUserAutoDto.getAdminUserRole());
	}

	@Override
	public AdminUser getAdminUser(Long adminUserId) {
		return AdminUser.from(adminUserPort.getAdminUser(adminUserId));
	}

	@Override
	public List<AdminUser> getAdminUserList(String email, String name, String phoneNumber,
		List<SortDto<AdminUserSortStandard>> sorts, Long pageNo, Integer pageSize) {
		return adminUserPort.getAdminUserList(email, name, phoneNumber, sorts, pageNo, pageSize)
			.stream()
			.map(AdminUser::from)
			.toList();
	}
}
