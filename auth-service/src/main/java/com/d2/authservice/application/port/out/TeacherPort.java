package com.d2.authservice.application.port.out;

import com.d2.core.model.domain.AdminUserAuth;

public interface TeacherPort {
	void register(AdminUserAuth adminUserAuth, String name, String email, String phoneNumber);
}
