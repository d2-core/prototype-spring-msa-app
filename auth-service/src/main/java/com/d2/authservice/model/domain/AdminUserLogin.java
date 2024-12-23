package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserRole;
import com.d2.authservice.model.enums.AdminUserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserLogin {
	private Long id;

	private AdminUserRole adminUserRole;

	private String nickname;

	private String email;

	private String phoneNumber;

	private AdminUserStatus status;

	private LocalDateTime registeredAt;

	private LocalDateTime lastLoginAt;

	private final Token token;

	public static AdminUserLogin from(AdminUserDto adminUserDto, TokenDto accessToken, TokenDto refreshToken) {
		Token token = Token.builder()
			.accessToken(accessToken.getToken())
			.accessTokenExpiredAt(accessToken.getExpiredAt())
			.refreshToken(refreshToken.getToken())
			.refreshTokenExpiredAt(refreshToken.getExpiredAt())
			.build();
		return new AdminUserLogin(
			adminUserDto.getId(),
			adminUserDto.getAdminUserRole(),
			adminUserDto.getNickname(),
			adminUserDto.getEmail(),
			adminUserDto.getPhoneNumber(),
			adminUserDto.getStatus(),
			adminUserDto.getRegisteredAt(),
			adminUserDto.getRegisteredAt(),
			token
		);
	}
}
