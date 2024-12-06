package com.d2.authservice.model.dto;

import java.time.LocalDateTime;

import com.d2.authservice.adapter.out.persistence.user.UserJpaEntity;
import com.d2.authservice.model.enums.UserStatus;
import com.d2.core.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;

	private Role role;

	private String nickname;

	private String email;

	private String phoneNumber;

	private UserStatus status;

	private LocalDateTime registeredAt;

	private LocalDateTime modifiedAt;

	private LocalDateTime lastLoginAt;

	public static UserDto from(UserJpaEntity userJpaEntity) {
		return new UserDto(
			userJpaEntity.getId(),
			userJpaEntity.getRole(),
			userJpaEntity.getNickname(),
			userJpaEntity.getEmail(),
			userJpaEntity.getPhoneNumber(),
			userJpaEntity.getStatus(),
			userJpaEntity.getRegisteredAt(),
			userJpaEntity.getModifiedAt(),
			userJpaEntity.getLastLoginAt()
		);
	}
}
