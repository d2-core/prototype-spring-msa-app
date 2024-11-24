package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.TokenUseCase;
import com.d2.authservice.model.domain.AdminUserTokenClaims;
import com.d2.authservice.model.domain.UserTokenClaims;
import com.d2.authservice.model.request.AdminUserValidateTokenRequest;
import com.d2.core.api.API;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenController {

	private final TokenUseCase tokenUseCase;

	@PostMapping("auth/v1/tokens/admin-users/validate")
	public API<AdminUserTokenClaims> validateToken(
		@Valid @RequestBody AdminUserValidateTokenRequest adminUserValidateTokenRequest
	) {
		return API.OK(tokenUseCase.validateTokenForAdminUser(adminUserValidateTokenRequest.getJwtToken()));
	}

	@PostMapping("auth/v1/tokens/users/validate")
	public API<UserTokenClaims> validateTokenForUser() {
		return API.OK(tokenUseCase.validateTokenForUser("TOKEN"));
	}
}
