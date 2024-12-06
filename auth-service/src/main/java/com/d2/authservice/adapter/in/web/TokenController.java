package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.TokenUseCase;
import com.d2.authservice.model.domain.Token;
import com.d2.authservice.model.domain.TokenClaims;
import com.d2.authservice.model.request.ValidateTokenRequest;
import com.d2.core.api.API;
import com.d2.core.constant.HeaderConstant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenController {

	private final TokenUseCase tokenUseCase;

	@PostMapping("auth/v1/tokens/validate")
	public API<TokenClaims> validateTokenForUser(@RequestBody ValidateTokenRequest validateTokenRequest) {
		return API.OK(tokenUseCase.validateToken(validateTokenRequest.getAccessToken()));
	}

	@PostMapping("auth/v1/tokens/refresh")
	public API<Token> refreshToken(@RequestHeader(value = HeaderConstant.X_D2_REFRESH) String refreshToken) {
		return API.OK(tokenUseCase.refreshToken(refreshToken));
	}
}
