package com.d2.authservice.application.port.out;

import com.d2.core.model.enums.AdminUserRole;
import com.d2.core.model.enums.TokenRole;

public interface SendPort {
	void sendAdminUserSignupEvent(Long adminUserId, TokenRole tokenRole, AdminUserRole adminUserRole);
}
