package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.dto.UserDto;
import com.d2.authservice.model.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLogin {
	private final Long id;

	private final String nickname;

	private final String email;

	private final String phoneNumber;

	private final UserStatus status;

	private final LocalDateTime registeredAt;

	private final LocalDateTime modifiedAt;

	private final LocalDateTime lastLoginAt;

	private final Token token;

	public static UserLogin from(UserDto userDto, TokenDto accessTokenDto, TokenDto refreshTokenDto) {
		Token token = new Token(
			accessTokenDto.getToken(),
			accessTokenDto.getExpiredAt(),
			refreshTokenDto.getToken(),
			refreshTokenDto.getExpiredAt()
		);
		return new UserLogin(
			userDto.getId(),
			userDto.getNickname(),
			userDto.getEmail(),
			userDto.getPhoneNumber(),
			userDto.getStatus(),
			userDto.getRegisteredAt(),
			userDto.getModifiedAt(),
			userDto.getLastLoginAt(),
			token
		);
	}
}
