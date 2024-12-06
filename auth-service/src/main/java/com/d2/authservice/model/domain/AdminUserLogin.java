package com.d2.authservice.model.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.core.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserLogin {
	private Long id;

	private Role role;

	private List<AdminUserPermission> permissions;

	private String name;

	private String email;

	private String password;

	private String phoneNumber;

	private AdminUserStatus status;

	private LocalDateTime registeredAt;

	private LocalDateTime lastLoginAt;

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
			adminUserDto.getRole(),
			adminUserDto.getPermissions(),
			adminUserDto.getName(),
			adminUserDto.getEmail(),
			adminUserDto.getPassword(),
			adminUserDto.getPhoneNumber(),
			adminUserDto.getStatus(),
			adminUserDto.getRegisteredAt(),
			adminUserDto.getRegisteredAt(),
			token
		);
	}
}
