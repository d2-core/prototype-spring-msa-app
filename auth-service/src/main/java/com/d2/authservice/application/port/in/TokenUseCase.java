package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.Token;
import com.d2.authservice.model.domain.TokenClaims;

public interface TokenUseCase {
	TokenClaims validateToken(String accessToken);

	Token refreshToken(String refreshToken);
}
