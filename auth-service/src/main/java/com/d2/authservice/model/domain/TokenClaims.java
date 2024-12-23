package com.d2.authservice.model.domain;

import com.d2.core.model.enums.TokenRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenClaims {
	private final TokenRole tokenRole;
	private final Long id;

	public static TokenClaims from(TokenRole tokenRole, Long id) {
		return new TokenClaims(tokenRole, id);
	}
}
