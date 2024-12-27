package com.d2.authservice.model.dto;

import com.d2.core.model.enums.AdminUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserAutoDto {
	private Long id;

	private AdminUserRole adminUserRole;
}
