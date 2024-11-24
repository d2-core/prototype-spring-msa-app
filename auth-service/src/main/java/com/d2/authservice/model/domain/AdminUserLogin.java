package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserLogin {
	private final Long id;

	private final String name;

	private final String email;

	private final String phoneNumber;

	private final AdminUserStatus status;

	private final LocalDateTime registeredAt;

	private final LocalDateTime lastLoginAt;

	private final Token token;

	@Data
	@Builder
	@AllArgsConstructor
	public static class Token {
		private final String accessToken;
		private final LocalDateTime accessExpiredAt;
		private final String refreshToken;
		private final LocalDateTime refreshExpiredAt;
	}

	public static AdminUserLogin from(AdminUserDto adminUserDto, TokenDto accessToken, TokenDto refreshToken) {
		Token token = Token.builder()
			.accessToken(accessToken.getToken())
			.accessExpiredAt(accessToken.getExpiredAt())
			.refreshToken(refreshToken.getToken())
			.refreshExpiredAt(refreshToken.getExpiredAt())
			.build();
		return new AdminUserLogin(
			adminUserDto.getId(),
			adminUserDto.getName(),
			adminUserDto.getEmail(),
			adminUserDto.getPhoneNumber(),
			adminUserDto.getStatus(),
			adminUserDto.getRegisteredAt(),
			adminUserDto.getLastLoginAt(),
			token
		);
	}
}
