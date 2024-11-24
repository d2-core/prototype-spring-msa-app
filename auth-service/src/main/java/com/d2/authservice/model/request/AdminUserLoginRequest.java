package com.d2.authservice.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserLoginRequest {
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;
}
