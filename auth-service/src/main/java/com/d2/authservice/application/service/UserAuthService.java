package com.d2.authservice.application.service;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.UserAuthUseCase;
import com.d2.authservice.model.domain.UserLogin;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserAuthService implements UserAuthUseCase {

	@Override
	public UserLogin loginWithKakao() {
		return null;
	}

	@Override
	public UserLogin loginWithGithub() {
		return null;
	}

	@Override
	public void logout(Long userId) {

	}
}
