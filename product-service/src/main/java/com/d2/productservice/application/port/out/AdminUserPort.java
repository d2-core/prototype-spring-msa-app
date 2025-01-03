package com.d2.productservice.application.port.out;

import com.d2.productservice.model.dto.AdminUserDto;

public interface AdminUserPort {

	AdminUserDto getAdminUser(Long adminUserId);
}
