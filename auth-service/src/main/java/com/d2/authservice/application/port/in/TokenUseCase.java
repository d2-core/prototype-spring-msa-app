package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.AdminUserTokenClaims;
import com.d2.authservice.model.domain.UserTokenClaims;

public interface TokenUseCase {

	AdminUserTokenClaims validateTokenForAdminUser(String jwtToken);

	UserTokenClaims validateTokenForUser(String jwtToken);
}
