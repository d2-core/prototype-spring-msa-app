package com.d2.core.model.domain;

import com.d2.core.model.enums.TokenRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuth {
	private Long userId;
	private TokenRole tokenRole;

	public UserAuth(Long userId) {
		this.userId = userId;
	}
}
