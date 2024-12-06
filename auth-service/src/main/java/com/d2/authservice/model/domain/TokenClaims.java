package com.d2.authservice.model.domain;

import com.d2.core.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenClaims {
	private final Role role;
	private final Long id;

	public static TokenClaims from(Role role, Long id) {
		return new TokenClaims(role, id);
	}
}
