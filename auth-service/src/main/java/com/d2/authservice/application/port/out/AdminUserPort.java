package com.d2.authservice.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.model.dto.AdminUserAutoDto;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.enums.AdminUserSortStandard;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.dto.SortDto;
import com.d2.core.model.enums.AdminUserRole;

public interface AdminUserPort {
	Boolean existEmailOrPhoneNumber(String email, String phoneNumber);

	AdminUserDto register(AdminUserRole adminUserRole, String nickname, String email, String password,
		String phoneNumber, AdminUserStatus status, LocalDateTime lastLoginAt);

	AdminUserDto getAdminUserByEmailAndPasswordWithThrow(String email, String password);

	AdminUserAutoDto getAdminUserAuth(Long id);

	AdminUserDto getAdminUser(Long id);

	List<AdminUserDto> getAdminUserList(String email, String name, String phoneNumber,
		List<SortDto<AdminUserSortStandard>> sortList, Long pageNo, Integer pageSize);
}
