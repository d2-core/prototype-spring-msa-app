package com.d2.authservice.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserLoginRequest {
	@Email
	private String email;

	@NotEmpty
	private String password;
}
