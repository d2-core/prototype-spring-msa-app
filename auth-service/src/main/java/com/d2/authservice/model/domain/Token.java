package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.TokenDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
	private final String token;
	private final LocalDateTime expiredAt;

	public static Token from(TokenDto accessToken) {
		return new Token(
			accessToken.getToken(),
			accessToken.getExpiredAt()
		);
	}
}
