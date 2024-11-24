package com.d2.authservice.application.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.authservice.AdminUserConstant;
import com.d2.authservice.application.port.in.AdminUserAuthUseCase;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.error.AdminUserErrorCodeImpl;
import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminUserAuthService implements AdminUserAuthUseCase {
	private final TokenPort tokenPort;
	private final AdminUserPort adminUserPort;
	private final SmsVerificationPort smsVerificationPort;

	@Override
	public AdminUserLogin register(String name, String email, String password, String phoneNumber, String authCode) {
		if (adminUserPort.existEmail(email)) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.EXIST_EMAIL, "email: %s".formatted(email));
		}

		if (adminUserPort.existPhoneNumber(phoneNumber)) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.EXIST_PHONE_NUMBER,
				"phoneNumber: %s".formatted(phoneNumber));
		}

		if (!smsVerificationPort.existSmsVerification(phoneNumber, authCode,
			SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS)) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.NOT_COMPLETED_PHONE_AUTH,
				"phoneNumber: %s, authCode: %s".formatted(phoneNumber, authCode));
		}

		LocalDateTime lastLoginAt = LocalDateTime.now();
		AdminUserDto adminUserDto = adminUserPort.register(name, Role.ADMIN, AdminUserPermission.READ, email, password,
			phoneNumber, AdminUserStatus.REGISTERED, lastLoginAt);

		Map<String, Object> data = getAdminUserClaimData(adminUserDto);
		TokenDto accessTokenDto = tokenPort.issueAccessToken(data);

		TokenDto refreshTokenDto = tokenPort.issueRefreshToken(data);
		return AdminUserLogin.from(adminUserDto, accessTokenDto, refreshTokenDto);
	}

	@Override
	public AdminUserLogin login(String email, String password) {
		AdminUserDto adminUserDto = new AdminUserDto();

		try {
			adminUserDto = adminUserPort.getAdminUserByEmailAndPasswordWithThrow(email, password);
		} catch (ApiExceptionImpl e) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.EMAIL_OR_PASSWORD_NOT_MATCHED, e,
				"email: %s, password: %s".formatted(email, password));
		}

		Map<String, Object> data = getAdminUserClaimData(adminUserDto);

		TokenDto accessTokenDto = tokenPort.issueAccessToken(data);
		TokenDto refreshTokenDto = tokenPort.issueRefreshToken(data);

		return AdminUserLogin.from(adminUserDto, accessTokenDto, refreshTokenDto);
	}

	public Map<String, Object> getAdminUserClaimData(AdminUserDto adminUserDto) {
		Map<String, Object> data = new HashMap<>();

		data.put(AdminUserConstant.ROLE, adminUserDto.getRole().name());
		data.put(AdminUserConstant.ADMIN_USER_ID, String.valueOf(adminUserDto.getId()));
		data.put(AdminUserConstant.ADMIN_USER_PERMISSIONS, adminUserDto.getPermissions());

		return data;
	}
}
