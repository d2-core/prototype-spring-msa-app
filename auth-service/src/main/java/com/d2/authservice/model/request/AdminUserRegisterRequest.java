package com.d2.authservice.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserRegisterRequest {

	@NotEmpty
	private String name;

	@Email
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String phoneNumber;

	@NotEmpty
	private String authCode;
}