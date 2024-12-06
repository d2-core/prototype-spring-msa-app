package com.d2.authservice.application.port.in;

import java.util.List;

import com.d2.authservice.model.domain.AdminUser;
import com.d2.authservice.model.enums.AdminUserSortStandard;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.dto.SortDto;

public interface FindAdminUserUseCase {
	AdminUserAuth getAdminUserAuth(Long adminUserId);

	AdminUser getAdminUser(Long adminUserId);

	List<AdminUser> getAdminUserList(String email, String name, String phoneNumber,
		List<SortDto<AdminUserSortStandard>> sortList, Long pageNo, Integer pageSize);
}
