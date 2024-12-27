package com.d2.authservice.model.request;

import com.d2.core.model.enums.AdminUserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserRegisterRequest {

	@NotNull
	private AdminUserRole adminUserRole;

	@NotEmpty
	private String nickname;

	@Email
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String phoneNumber;

	@NotEmpty
	private String authCode;

	@NotNull
	private Long checkAuthCodeId;
}
