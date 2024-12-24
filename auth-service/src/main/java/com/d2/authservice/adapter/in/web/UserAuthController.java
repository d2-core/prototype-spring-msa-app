package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.UserAuthUseCase;
import com.d2.authservice.model.domain.UserLogin;
import com.d2.authservice.model.request.UserKaKaoLoginRequest;
import com.d2.core.api.API;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class UserAuthController {
	private final UserAuthUseCase userAuthUseCase;

	@PostMapping("v1/users/login-with-kakao")
	public API<UserLogin> loginWithKakao(@RequestBody UserKaKaoLoginRequest kaKaoLoginRequest) {
		return API.OK(userAuthUseCase.loginWithKakao(kaKaoLoginRequest.getCode()));
	}
}
