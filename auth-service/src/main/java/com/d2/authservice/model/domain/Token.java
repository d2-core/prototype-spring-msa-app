package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.TokenDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Token {
	private final String accessToken;
	private final LocalDateTime accessTokenExpiredAt;
	private final String refreshToken;
	private final LocalDateTime refreshTokenExpiredAt;

	public static Token from(TokenDto accessToken, TokenDto refreshToken) {
		return new Token(
			accessToken.getToken(),
			accessToken.getExpiredAt(),
			refreshToken.getToken(),
			refreshToken.getExpiredAt()
		);
	}
}
