package com.d2.productservice.model.dto;

import com.d2.core.model.enums.AdminUserRole;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserDto {
	private Long id;

	private AdminUserRole adminUserRole;

	private String nickname;

	private String email;

	private String phoneNumber;
}
