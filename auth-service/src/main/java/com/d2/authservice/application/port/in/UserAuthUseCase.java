package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.UserLogin;

public interface UserAuthUseCase {

	UserLogin loginWithKakao(String code);
}
