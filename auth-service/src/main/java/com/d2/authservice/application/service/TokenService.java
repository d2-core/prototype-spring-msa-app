package com.d2.authservice.application.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.TokenUseCase;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.constant.TokenConstant;
import com.d2.authservice.model.domain.Token;
import com.d2.authservice.model.domain.TokenClaims;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.core.model.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService implements TokenUseCase {
	private final TokenPort tokenPort;

	@Override
	public TokenClaims validateToken(String jwtToken) {
		Map<String, Object> data = tokenPort.validateTokenWithThrow(jwtToken);

		Role role = Role.valueOf(data.get(TokenConstant.ROLE).toString());
		Long id = Long.valueOf(data.get(TokenConstant.ID).toString());

		return TokenClaims.from(role, id);
	}

	@Override
	public Token refreshToken(String refreshToken) {
		Map<String, Object> data = tokenPort.validateTokenWithThrow(refreshToken);
		TokenDto tokenDto = tokenPort.issueAccessToken(data);
		return Token.from(tokenDto);
	}
}
